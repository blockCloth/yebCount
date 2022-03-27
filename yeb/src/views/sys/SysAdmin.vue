<template>
  <div>
    <div style="display: flex;justify-content: center">
      <el-input v-model="workName" placeholder="请输入用户名进行搜索..." size="small"
                icon="el-icon-search" style="width: 400px;margin-right: 8px"></el-input>
      <el-button type="primary" icon="el-icon-search" size="small" @click="doSearch">搜索</el-button>
    </div>
    <div class="admin-container">
      <el-card class="admin-card" v-for="(admin,index) in admins" :key="index">
        <div slot="header" class="clearfix">
          <span>{{ admin.name }}</span>
          <el-button style="float: right; padding: 3px 0;color: red" type="text"
                     icon="el-icon-delete" @click="deleteAdmin(admin)"></el-button>
        </div>
        <div class="user-container">
          <div>用户名：{{ admin.name }}</div>
          <div>手机号码：{{ admin.phone }}</div>
          <div>电话号码：{{ admin.telephone }}</div>
          <div>地址：{{ admin.address }}</div>
          <div>
            <el-tag>是否启用</el-tag>
            <el-switch
                v-model="admin.enabled"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="已启用"
                inactive-text="未启用" style="margin-left: 7px">
            </el-switch>
          </div>
          <div>
            用户角色：
            <el-tag style="margin-right: 8px" type="success" v-for="(role,index) in admin.roles" :key="index">
              {{ role.nameZh }}
            </el-tag>
            <el-popover
                placement="right"
                title="角色列表"
                width="200"
                @show="showRoles(admin)"
                @hide="hideAdmin(admin)"
                trigger="click">
              <el-select v-model="selectedRoles" multiple placeholder="请选择角色" >
                <el-option
                    v-for="(r,index) in allRoles"
                    :key="index"
                    :label="r.nameZh"
                    :value="r.id">
                </el-option>
              </el-select>
              <el-button slot="reference" type="text" icon="el-icon-more" size="mini"></el-button>
            </el-popover>
          </div>
          <div>
            备注：{{ admin.remark }}
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "SysAdmin",
  data() {
    return {
      admin: {
        name: '',
      },
      admins: [],
      workName: '',
      allRoles:[],
      selectedRoles:[]
    }
  },
  mounted() {
    this.initAdmin();
  },
  methods: {
    //修改操作员角色
    hideAdmin(admin){
      //先判断是否需要更新角色
      let flag = false;
      //获取所有角色
      let roles = [];
      Object.assign(roles,admin.roles);
      //判断角色个数跟用户选择是否一致
      if (roles.length != this.selectedRoles.length){
        flag = true;
      }else {
        for (let i = 0; i < roles.length; i++) {
          //获取到角色数组当中的角色
          let role = roles[i];
          for (let j = 0; j < this.selectedRoles.length; j++) {
              //获取到选择数组的id
              let sr = this.selectedRoles[j];
              //判断id是否一致
              if (role.id == sr){
                roles.splice(i,1);
                i--;
                break;
              }
          }
        }
        //判断角色数组长度是否一致
        if (roles.length != 0){
          flag = true;
        }
      }
      //

      if (flag){
        let url = '/system/admin/role/' + admin.id + "?";
        this.selectedRoles.forEach(sr =>{
          url += "rids=" +  sr + "&"
        })
        this.putRequest(url).then(resp=>{
          if (resp){
            this.selectedRoles = []
            this.initAdmin();
          }
        })
      }
    },
    //角色视图
    showRoles(admin){
      this.initRoles();
      let roles = admin.roles;
      this.selectedRoles = [];
      roles.forEach(r=>{
        this.selectedRoles.push(r.id);
      })
    },
    //初始化角色信息
    initRoles(){
      this.getRequest('/system/admin/role').then(resp => {
        if (resp) {
          this.allRoles = resp;
        }
      })
    },
    //删除角色
    deleteAdmin(admin) {
      this.$confirm('此操作将永久删除该[' + admin.name + ']操作员, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/admin/' + admin.id).then(resp => {
          if (resp) {
            this.initAdmin();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //通过用户名搜索用户
    doSearch() {
      this.initAdmin();
    },
    // 获取操作员
    initAdmin() {
      this.getRequest('/system/admin/?workName=' + this.workName).then(resp => {
        if (resp) {
          this.admins = resp;
        }
      })
    }
  }
}
</script>

<style>
.admin-card {
  width: 450px;
  margin-bottom: 20px;
}

.admin-container {
  display: flex;
  margin-top: 10px;
  justify-content: space-around;
  flex-wrap: wrap;
}

.user-container {
  font-size: 14px;
  line-height: 30px;
  color: #53a8ff;
}
</style>