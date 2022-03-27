<template>
  <div>
    <el-container>
      <el-header class="HomeHead">
        <div class="titel">
          云E办系统
        </div>
        <div>
          <el-button type="text" icon="el-icon-s-comment"
                     size="thanks"
                    style="color: #ffffff;margin-right: 8px;size: 20px"
                    @click="goChat"></el-button>
          <el-dropdown @command="commandHead">
          <span class="el-dropdown-link">
            {{ user.name }}
          </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="userInfo">个人资料</el-dropdown-item>
              <el-dropdown-item command="set">设置</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu router unique-opened>
            <el-submenu :index="index + ''" v-for="(item,index) in routes" v-if="!item.hidden" :key="index">
              <template slot="title">
                <i :class="item.iconCls" style="color: #5ec8ff;padding-right: 5px"></i>
                <span>{{ item.name }}</span>
              </template>
              <el-menu-item :index="children.path" v-for="(children,indexN) in item.children" :key="indexN">
                <span>{{ children.name }}</span>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right"
                         v-if="this.$router.currentRoute.path != '/home'">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ this.$router.currentRoute.name }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div class="HomeWelcome" v-if="this.$router.currentRoute.path == '/home'">
                  欢迎来到云E办系统
          </div>
          <router-view class="HomeRouter"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      //user: JSON.parse(window.sessionStorage.getItem("user"))
    }
  },
  computed: {
    routes() {
      return this.$store.state.routes;
    },
    user(){
      return this.$store.state.currentAdmin;
    }
  },
  methods: {
    //前往聊天页面
    goChat(){
      this.$router.push('/chat');
    },
    commandHead(command) {
      //判断是否为用户退出
      if (command == "logout"){
        //提示用户是否退出
        this.$confirm('是否退出用户?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //退出用户
          this.getRequest("/logout");
          //清除token信息
          window.sessionStorage.removeItem("tokenStr");
          //清除用户信息
          window.sessionStorage.removeItem("user");
          //清除菜单信息
          this.$store.commit("initRoutes",[]);
          //返回到登录页
          this.$router.replace("/");
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
      //判断用户是否查看个人信息
      if (command == "userInfo"){
        this.$router.push('/userinfo');
      }
    }
  }
}
</script>

<style scoped>
.HomeHead {
  background-color: #6a8aff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-right: 0px 15px;
  box-sizing: border-box;
}

.HomeHead .titel {
  font-size: 30px;
  color: white;
  font-family: 华文行楷;
}
.HomeWelcome{
  font-size: 50px;
  color: #6a8aff;
  font-family: 华文行楷;
  text-align: center;
  padding-top: 50px;
}
.HomeRouter{
  margin-top: 15px;
}
</style>