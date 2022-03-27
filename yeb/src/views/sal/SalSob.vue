<template>
  <div>
    <div style="display: flex;justify-content: space-between">
      <el-button type="primary" icon="el-icon-plus" @click="showSalayView">添加工资账套</el-button>
      <el-button type="success" icon="el-icon-refresh"></el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="salaries"
          border
          stripe
          style="width: 100%">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="name"
            label="账套名称"
            width="120">
        </el-table-column>
        <el-table-column
            prop="basicSalary"
            label="基本工资"
            width="70">
        </el-table-column>
        <el-table-column
            prop="lunchSalary"
            label="午餐补助"
            width="70">
        </el-table-column>
        <el-table-column
            prop="trafficSalary"
            label="交通补助"
            width="70">
        </el-table-column>
        <el-table-column
            prop="bonus"
            label="奖金"
            width="70">
        </el-table-column>
        <el-table-column label="养老金" align="center">
          <el-table-column
              prop="pensionBase"
              label="基数"
              width="70">
          </el-table-column>
          <el-table-column
              prop="pensionPer"
              label="比例"
              width="70">
          </el-table-column>
        </el-table-column>
        <el-table-column label="医疗金" align="center">
          <el-table-column
              prop="medicalBase"
              label="基数"
              width="70">
          </el-table-column>
          <el-table-column
              prop="medicalPer"
              label="比例"
              width="70">
          </el-table-column>
        </el-table-column>
        <el-table-column label="公积金" align="center">
          <el-table-column
              prop="accumulationFundBase"
              label="基数"
              width="70">
          </el-table-column>
          <el-table-column
              prop="accumulationFundPer"
              label="比例"
              width="70">
          </el-table-column>
        </el-table-column>
        <el-table-column
            prop="createDate"
            align="center"
            label="启用时间"
            width="170">
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="primary" style="margin-right: 5px"
                        @click="showSalayEdit(scope.row)">编辑</el-button>
            <el-button type="danger" @click="deleteSalary(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        width="50%">

      <div class="salaryView">
        <el-steps direction="vertical" :active="activeItemIndex">
          <el-step :title="itemName" v-for="(itemName,index) in salaryItemName" :key="index"></el-step>
        </el-steps>
        <el-input :placeholder="'请输入' + salaryItemName[index] + '...'"
                  v-model="salary[title]"
                  v-for="(value,title,index) in salary" :key="index"
                  v-show="index == activeItemIndex"
                  style="width: 200px"/>
      </div>

      <span slot="footer" class="dialog-footer">
      <el-button @click="preStep">{{ activeItemIndex == 10 ? '取消' : '上一步' }}</el-button>
      <el-button type="primary"
                 @keydown.enter.native="nextStep"
                 @click="nextStep">{{ activeItemIndex == 10 ? '完成' : '下一步' }}</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "SalSob",
  data() {
    return {
      dialogTitle: '',
      dialogVisible: false,
      salaries: [],
      activeItemIndex: 0,
      salaryItemName: [
        '账套名称',
        '基本工资',
        '午餐补助',
        '交通补助',
        '奖金',
        '养老金基数',
        '养老金比例',
        '医疗金基数',
        '医疗金比例',
        '公积金基数',
        '公积金比例',
      ],
      salary: {
        name: '',
        basicSalary: null,
        lunchSalary: null,
        trafficSalary: null,
        bonus: null,
        pensionBase: null,
        pensionPer: null,
        medicalBase: null,
        medicalPer: null,
        accumulationFundBase: null,
        accumulationFundPer: null,
        id: null
      }
    }
  },
  mounted() {
    this.initSalary();
  },
  methods: {
    //编辑工资账套视图
    showSalayEdit(data){
      this.dialogTitle = '编辑工资账套';
      this.salary.name = data.name;
      this.salary.basicSalary = data.basicSalary;
      this.salary.lunchSalary = data.lunchSalary;
      this.salary.trafficSalary = data.trafficSalary;
      this.salary.bonus = data.bonus;
      this.salary.pensionBase = data.pensionBase;
      this.salary.pensionPer = data.pensionPer;
      this.salary.medicalBase = data.medicalBase;
      this.salary.medicalPer = data.medicalPer;
      this.salary.accumulationFundBase = data.accumulationFundBase;
      this.salary.accumulationFundPer = data.accumulationFundPer;
      this.salary.id = data.id;

      this.dialogVisible = !this.dialogVisible;
      this.activeItemIndex = 0;

    },
    //删除工资账套
    deleteSalary(index, data) {
      this.$confirm('此操作将永久删除[' + data.name + ']该账套, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/salary/sob/' + data.id).then(resp => {
          if (resp) {
            this.initSalary();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //步骤条上一步
    preStep() {
      if (this.activeItemIndex == 10) {
        this.dialogVisible = false;
        return;
      }
      if (this.activeItemIndex == 0) {
        return;
      }
      this.activeItemIndex--;
    },
    //步骤条下一步
    nextStep() {
      if (this.activeItemIndex == 10) {
        if (this.salary.id){
          this.putRequest('/salary/sob/', this.salary).then(resp => {
            if (resp) {
              this.initSalary();
              this.dialogVisible = false;
            }
          })
        }else {
          this.postRequest('/salary/sob/', this.salary).then(resp => {
            if (resp) {
              this.initSalary();
              this.dialogVisible = false;
            }
          })
        }
        return;
      }
      this.activeItemIndex++;
    },
    //添加工资账套视图
    showSalayView() {
      this.dialogTitle = '添加工资账套';
      this.activeItemIndex = 0;
      this.dialogVisible = !this.dialogVisible;
      this.salary = {
        name: '',
        basicSalary: null,
        lunchSalary: null,
        trafficSalary: null,
        bonus: null,
        pensionBase: null,
        pensionPer: null,
        medicalBase: null,
        medicalPer: null,
        accumulationFundBase: null,
        accumulationFundPer: null,
        id: null
      };
    },
    //获取账套
    initSalary() {
      this.getRequest('/salary/sob/').then(resp => {
        if (resp) {
          this.salaries = resp;
        }
      })
    }
  }
}
</script>

<style>
.salaryView {
  display: flex;
  justify-content: space-around;
  align-items: center;
}
</style>