<template>
  <div>
    <div class="permissManaTool">
      <el-input size="small" placeholder="请输入角色英文名" v-model="role.name">
        <template slot="prepend">ROLE_</template>
      </el-input>
      <el-input size="small" placeholder="请输入角色中文名" v-model="role.nameZh"></el-input>
      <el-button size="small" type="primary" icon="el-icon-circle-plus" @click="insertRole">添加角色</el-button>
    </div>
    <div class="permissManaMain">
      <el-collapse v-model="activeName" accordion @change="change">
        <el-collapse-item :title="r.nameZh" :name="r.id" v-for="(r,index) in roles" :key="index">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>可访问资源</span>
              <el-button style="float: right;padding: 3px 0;color: red" type="text"
                         icon="el-icon-delete" @click="deleteRole(r.nameZh,r.id)"></el-button>
            </div>
            <div>
              <el-tree show-checkbox
                       ref="tree"
                       :data="allMenus"
                       :props="defaultProps"
                       :default-checked-keys=selectMenus
                       node-key="id"></el-tree>
            </div>
            <div style="display: flex;justify-content: flex-end">
              <el-button size="mini" @click="doCancel">取消操作</el-button>
              <el-button size="mini" type="primary" @click="updateMenu(r.id,index)">确认操作</el-button>
            </div>
          </el-card>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script>
export default {
  name: "PermissMana",
  data() {
    return {
      role: {
        name: '',
        nameZh: ''
      },
      roles: [],
      allMenus: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      selectMenus: [],
      activeName: ''
    }
  },
  mounted() {
    this.initRoles();
  },
  methods: {
    //删除用户信息
    deleteRole(rNameZh,rid){
      this.$confirm('此操作将永久删除['+rNameZh+']角色？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          this.deleteRequest("/system/basic/permiss/role/"+rid).then(resp=>{
            if (resp){
              this.initRoles();
            }
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //添加角色信息
    insertRole() {
        //判断用户名是否为空
        if (this.role.name && this.role.nameZh){
          this.postRequest("/system/basic/permiss/",this.role).then(resp=>{
            if (resp){
              this.initRoles();
            }
          })
        }else {
          this.$message.error("角色名不能为空！");
        }
    },
    //取消操作
    doCancel() {
      this.activeName = -1;
    },
    //修改菜单信息
    updateMenu(rid, index) {
      //获取选择到的菜单id
      let selectKeys = this.$refs.tree[index].getCheckedKeys(true);
      //拼接url
      let url = '/system/basic/permiss/role/' + rid + "?"
      //循环数组
      selectKeys.forEach(item => {
        url += "mids=" + item + "&"
      })
      //修改菜单
      this.putRequest(url).then(resp => {
        if (resp) {
          this.initRoles();
          this.activeName = index;
        }
      })
    },
    //根据用户id查询菜单信息
    selectMenusById(rid) {
      this.getRequest('/system/basic/permiss/mid/' + rid).then(resp => {
        if (resp) {
          this.selectMenus = resp;
        }
      })
    },
    //手风琴change事件
    change(rid) {
      if (rid) {
        this.initAllMenus();
        this.selectMenusById(rid);
      }
    },
    //获取所有角色
    initRoles() {
      this.getRequest('/system/basic/permiss/').then(resp => {
        if (resp) {
          this.role.name = '';
          this.role.nameZh = '';
          this.roles = resp;
        }
      })
    },
    //获取所有菜单
    initAllMenus() {
      this.getRequest('/system/basic/permiss/menus/').then(resp => {
        if (resp) {
          this.allMenus = resp;
        }
      })
    }
  }
}
</script>

<style>
.permissManaTool {
  display: flex;
}

.permissManaTool .el-input {
  width: 300px;
  margin-right: 7px;
}

.permissManaMain {
  width: 720px;
  margin-top: 10px;
}
</style>