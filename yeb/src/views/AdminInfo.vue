<template>
  <div>
    <div style="display: flex;justify-content: center">
      <el-card class="box-card" style="width: 450px">
        <div slot="header" class="clearfix">
          <span>{{ admin.name }}</span>
        </div>
        <div style="line-height: 35px">
          <div>
            用户名：&nbsp&nbsp&nbsp
            <el-tag>{{ admin.username }}</el-tag>
          </div>
          <div>
            电话号码：
            <el-tag>{{ admin.telephone }}</el-tag>
          </div>
          <div>
            手机号码：
            <el-tag>{{ admin.phone }}</el-tag>
          </div>
          <div>
            居住地址：
            <el-tag>{{ admin.address }}</el-tag>
          </div>
          <div>
            用户标签：
            <el-tag v-for="(r,index) in admin.roles"
                    style="margin-left: 5px"
                    type="success"
                    :key="index">{{ r.nameZh }}
            </el-tag>
          </div>
          <div style="display: flex;justify-content: space-between;margin-top: 7px">
            <el-button type="primary" @click="showAdminView">修改信息</el-button>
            <el-button type="danger" @click="showUpdatePasswordView">修改密码</el-button>
          </div>
        </div>
      </el-card>
      <el-dialog
          title="修改用户信息"
          :visible.sync="dialogVisible"
          width="30%">
        <div>
          <el-tag>用户昵称</el-tag>
          <el-input size="mini" style="width: 200px;margin-left: 8px" v-model="adminUpdate.name"></el-input>
          <br><br>
          <el-tag>电话号码</el-tag>
          <el-input size="mini" style="width: 200px;margin-left: 8px" v-model="adminUpdate.telephone"></el-input>
          <br><br>
          <el-tag>手机号码</el-tag>
          <el-input size="mini" style="width: 200px;margin-left: 8px" v-model="adminUpdate.phone"></el-input>
          <br><br>
          <el-tag>居住地址</el-tag>
          <el-input size="mini" style="width: 200px;margin-left: 8px" v-model="adminUpdate.address"></el-input>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateAdminInfo">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog
          title="更新密码"
          :visible.sync="updateDialogVisible"
          width="30%">
        <div>
          <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
                   class="demo-ruleForm">
            <el-form-item label="旧密码" prop="oldPass">
              <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPass">
              <el-input type="password" v-model="ruleForm.newPass" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
              <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
              <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminInfo",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.newPass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      admin: [],
      adminUpdate: [],
      dialogVisible: false,
      updateDialogVisible: false,
      ruleForm: {
        newPass: '',
        checkPass: '',
        oldPass: ''
      },
      rules: {
        newPass: [
          {validator: validatePass, trigger: 'blur'}
        ],
        checkPass: [
          {validator: validatePass2, trigger: 'blur'}
        ],
        oldPass: [
          {validator: validatePass, trigger: 'blur'}
        ]
      }
    };
  },
  mounted() {
    this.initAdmin();
  },
  methods: {
    //修改密码
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.ruleForm.adminId = this.admin.id;
          this.putRequest('/adminInfo/updatePass',this.ruleForm).then(resp=>{
            if (resp){
              //更新密码成功后退出登录
              this.getRequest('/logout');
              window.sessionStorage.removeItem('user');
              window.sessionStorage.removeItem('tokenStr');
              this.$store.commit('initRoutes',[]);
              this.$router.replace('/');
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    //修改用户密码
    showUpdatePasswordView() {
      this.updateDialogVisible = true;
    },
    //修改用户信息
    updateAdminInfo() {
      this.adminUpdate.password = null;
      this.putRequest('/adminInfo/update', this.adminUpdate).then(resp => {
        if (resp) {
          this.dialogVisible = false;
          this.initAdmin();
        }
      })
    },
    //修改用户视图
    showAdminView() {
      this.dialogVisible = !this.dialogVisible;
      this.adminUpdate = Object.assign({}, this.admin);
    },
    //获取用户信息
    initAdmin() {
      this.getRequest('/queryAdmin').then(resp => {
        if (resp) {
          this.admin = resp;
          window.sessionStorage.setItem('user', JSON.stringify(resp));
          this.$store.commit('INIT_ADMIN', resp);
        }
      })
    }
  }
}
</script>

<style>

</style>