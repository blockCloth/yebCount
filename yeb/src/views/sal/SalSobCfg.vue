<template xmlns:el="http://www.w3.org/1999/html">
  <div>
    <div></div>
    <div>
      <el-table
          :data="empSalary"
          border
          stripe>
        <el-table-column
            fixed
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="name"
            label="姓名"
            align="center"
            width="80">
        </el-table-column>
        <el-table-column
            prop="workID"
            label="工号"
            align="center"
            width="120">
        </el-table-column>
        <el-table-column
            prop="email"
            label="电子邮箱"
            align="center"
            width="180">
        </el-table-column>
        <el-table-column
            prop="phone"
            label="手机号码"
            align="center"
            width="120">
        </el-table-column>
        <el-table-column
            prop="department.name"
            label="所属部门"
            align="center"
            width="120">
        </el-table-column>
        <el-table-column
            label="在职状态"
            align="center"
            width="80">
          <template slot-scope="scope">
            <el-tag type="success">{{ scope.row.workState }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="email"
            label="电子邮箱"
            align="center"
            width="180">
        </el-table-column>
        <el-table-column
            label="工资账套"
            align="center">
          <template slot-scope="scope">
            <el-tooltip placement="right" v-if="scope.row.salary">
              <div slot="content">
                <table class="selectedSalary">
                  <tr>
                    <td>基本工资：</td>
                    <td><el-tag>{{scope.row.salary.basicSalary}}</el-tag></td>
                  </tr>
                  <tr>
                    <td>午餐补助：</td>
                    <td><el-tag>{{scope.row.salary.lunchSalary}}</el-tag></td>
                  </tr>
                  <tr>
                    <td>交通补助：</td>
                    <td><el-tag>{{scope.row.salary.trafficSalary}}</el-tag></td>
                  </tr>
                  <tr>
                    <td>奖金：</td>
                    <td><el-tag>{{scope.row.salary.bonus}}</el-tag></td>
                  </tr>
                  <tr>
                    <td>养老金基数：</td>
                    <td><el-tag>{{scope.row.salary.pensionBase}}</el-tag></td>
                  </tr>
                  <tr>
                    <td>养老金比例：</td>
                    <td><el-tag>{{scope.row.salary.pensionPer}}</el-tag></td>
                  </tr>
                  <tr>
                    <td>医疗金基数：</td>
                    <td><el-tag>{{scope.row.salary.medicalBase}}</el-tag></td>
                  </tr>
                  <tr>
                    <td>医疗金比例：</td>
                    <td><el-tag>{{scope.row.salary.medicalPer}}</el-tag></td>
                  </tr>
                  <tr>
                    <td>公积金基数：</td>
                    <td><el-tag>{{scope.row.salary.accumulationFundBase}}</el-tag></td>
                  </tr>
                  <tr>
                    <td>公积金比例：</td>
                    <td><el-tag>{{scope.row.salary.accumulationFundPer}}</el-tag></td>
                  </tr>
                </table>
              </div>
              <el-tag type="success">{{ scope.row.salary.name }}</el-tag>
            </el-tooltip>
            <el-tag type="danger" v-else>暂未设置</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-popover
                placement="left"
                title="编辑工资账套"
                width="200"
                @show="showPopover(scope.row.salary)"
                @hide="updateSalaryEmp(scope.row)"
                trigger="click">
              <div>
                <el-select v-model="currentSalary" placeholder="请选择">
                  <el-option
                      v-for="item in salaries"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </div>
              <el-button slot="reference" type="danger" >修改工资账套</el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>

      <div style="display: flex;justify-content: flex-end;margin-top: 8px">
        <el-pagination
            @current-change="currentEmpPage"
            @size-change="sizeEmpPage"
            :page-sizes="[5,10,20,30,40,50,100]"
            background
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total">
        </el-pagination>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  name: "SalSobCfg",
  data() {
    return {
      empSalary: [],
      total: null,
      currentPage: 1,
      size: 10,
      salaries: [],
      currentSalary: null
    }
  },
  mounted() {
    this.initEmployessSalary();
    this.initSalaries();
  },
  methods: {
    //修改工资账套
    updateSalaryEmp(data){
      if (this.currentSalary && this.currentSalary != data.salary.id){
        this.putRequest('/salary/sobcfg/'+data.id+','+this.currentSalary).then(resp=>{
          if (resp){
            this.initEmployessSalary();
          }
        })
      }
    },
    //初始化选择器
    showPopover(data){
      if (data.id){
        this.currentSalary = data.id;
      }else {
        this.currentSalary = null;
      }
    },
    //获取工资账套
    initSalaries(){
      this.getRequest('/salary/sobcfg/salaryes').then(resp=>{
        if(resp){
          this.salaries = resp;
        }
      })
    },
    //点击查询条数发生改变
    sizeEmpPage(size) {
      this.size = size;
      this.initEmployessSalary();
    },
    //更换页数
    currentEmpPage(currentPage) {
      this.currentPage = currentPage;
      this.initEmployessSalary();
    },
    //获取员工工资账套
    initEmployessSalary() {
      this.getRequest('/salary/sobcfg/?currentPage=' + this.currentPage + "&size=" + this.size).then(resp => {
        if (resp) {
          this.empSalary = resp.data;
          this.total = resp.total;
        }
      })
    }
  }
}
</script>

<style>
  .selectedSalary{
    border: 1px;
    border-radius: 10px
  }
</style>