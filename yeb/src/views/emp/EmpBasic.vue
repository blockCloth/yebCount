<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div>
          <el-input placeholder="请输入员工名进行搜索..." size="small" prefix-icon="el-icon-search" v-model="empName"
                    style="width: 400px;margin-right: 8px" @keydown.enter.native="selectEmpName"
                    clearable
                    :disabled="showAdavancedDisabled"
                    @clear="initEmployees"></el-input>
          <el-button type="primary" icon="el-icon-search"
                     :disabled="showAdavancedDisabled"
                     @click="selectEmpName" @keydown.enter.native="selectEmpName">搜索
          </el-button>
          <el-button type="primary" @click="showAdavancedDisabled = !showAdavancedDisabled">
            <i :class="showAdavancedDisabled?'fa fa-angle-double-up':'fa fa-angle-double-down'"
                aria-hidden="true"></i>高级搜索
          </el-button>
        </div>
        <div>
          <el-upload
              style="display: inline-flex;margin-right: 8px"
              :headers="headers"
              :show-file-list="false"
              :before-upload="uploadFileStyle"
              :on-success="uploadSuccess"
              :on-error="uploadDefeated"
              :disabled="updateDataDisabled"
              action="/system/basic/employee/import">
            <el-button type="success" :icon="uploadFileIcon" :disabled="updateDataDisabled">
              {{ uploadFileText }}
            </el-button>
          </el-upload>

          <el-button type="success" @click="exportEmployee" icon="el-icon-download">导出员工</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="showEmpView">添加员工</el-button>
        </div>
      </div>
    </div>
    <transition name="slide-fade">
    <div v-show="showAdavancedDisabled" class="advancedSearch">
      <el-row >
        <el-col :span="5">
          政治面貌：
            <el-select v-model="searchValue.politicId" placeholder="请选择政治面貌..." style="width: 130px">
              <el-option
                  v-for="item in politicsStatus"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
        </el-col>
        <el-col :span="4">
            民族：
            <el-select v-model="searchValue.nationId" placeholder="请选择民族..." style="width: 130px">
              <el-option
                  v-for="item in nations"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
        </el-col>
        <el-col :span="4">
          职位：
          <el-select v-model="searchValue.posId" placeholder="请选择职位..." style="width: 130px">
            <el-option
                v-for="item in positions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          职称：
          <el-select v-model="searchValue.jobLevelId" placeholder="请选择职称..." style="width: 130px">
            <el-option
                v-for="item in joblevels"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="7" style="margin-top: 5px">
          聘用形式：
          <el-radio-group v-model="searchValue.engageForm" >
            <el-radio label="劳动合同">劳动合同</el-radio>
            <el-radio label="劳务合同">劳务合同</el-radio>
          </el-radio-group>
        </el-col>
      </el-row>

      <el-row style="margin-top: 10px">
        <el-col :span="5">
          选择部门：
          <el-popover
              placement="bottom"
              title="选择部门"
              width="200"
              trigger="manual"
              v-model="visible2">
            <el-tree
                :data="allDepartment"
                :props="defaultProps"
                @node-click="searchHandleNodeClick"
                default-expand-all
                ref="tree">
            </el-tree>
            <div slot="reference" class="selectedDepSearch" @click="showDepView2">
              {{ this.depName }}
            </div>
          </el-popover>
        </el-col>
        <el-col :span="10">
          入职日期：
          <el-date-picker
              v-model="searchValue.beginDateScope"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              unlink-panels
              value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-col>
        <el-col :span="5" :offset="4">
          <el-button @click="showAdavancedDisabled">取消</el-button>
          <el-button type="primary" icon="el-icon-search" @click="initEmployees('advanced')">搜索</el-button>
        </el-col>
      </el-row>

    </div>
    </transition>
    <div style="padding-top: 8px">
      <el-table
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          :data="employees"
          stripe
          border
          :row-style="{height:'100%'}">
        <el-table-column
            fixed
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            fixed
            prop="name"
            label="姓名"
            width="75">
        </el-table-column>
        <el-table-column
            prop="gender"
            label="性别"
            width="55">
        </el-table-column>
        <el-table-column
            prop="workID"
            label="工号"
            width="85">
        </el-table-column>
        <el-table-column
            prop="wedlock"
            label="婚姻状况"
            width="80">
        </el-table-column>
        <el-table-column
            prop="birthday"
            label="出生日期"
            width="120">
        </el-table-column>
        <el-table-column
            prop="idCard"
            label="身份证号"
            width="150">
        </el-table-column>
        <el-table-column
            prop="nation.name"
            label="民族"
            width="80">
        </el-table-column>
        <el-table-column
            prop="nativePlace"
            label="籍贯"
            width="120">
        </el-table-column>
        <el-table-column
            prop="politicsStatus.name"
            label="政治面貌"
            width="120">
        </el-table-column>
        <el-table-column
            prop="email"
            label="邮箱"
            align="left"
            width="180">
        </el-table-column>
        <el-table-column
            prop="phone"
            label="联系电话"
            align="left"
            width="100">
        </el-table-column>
        <el-table-column
            prop="department.name"
            label="所属部门"
            width="100">
        </el-table-column>
        <el-table-column
            prop="position.name"
            label="职位"
            width="100">
        </el-table-column>
        <el-table-column
            prop="joblevel.name"
            label="职称"
            width="100">
        </el-table-column>
        <el-table-column
            prop="address"
            label="地址"
            align="left"
            width="300">
        </el-table-column>
        <el-table-column
            prop="engageForm"
            label="聘用形式"
            width="80">
        </el-table-column>
        <el-table-column
            prop="tiptopDegree"
            label="最高学历"
            width="80">
        </el-table-column>
        <el-table-column
            prop="school"
            label="毕业院校"
            width="120">
        </el-table-column>
        <el-table-column
            prop="specialty"
            label="所属专业"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="workState"
            label="在职状态"
            width="55">
        </el-table-column>
        <el-table-column
            prop="beginDate"
            label="入职日期"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="conversionTime"
            label="转正日期"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="beginContract"
            label="合同起始日期"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="endContract"
            label="合同终止日期"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            label="合同期限"
            align="left"
            width="80">
          <template slot-scope="scope">
            <el-tag>{{ scope.row.contractTerm }}</el-tag>
            年
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            width="280px"
            fixed="right">
          <template slot-scope="scope">
            <el-button style="margin-right: 3px" size="mini" @click="compileEmp(scope.row)">编辑</el-button>
            <el-button style="margin-right: 3px" size="mini" type="primary">查询高级资料</el-button>
            <el-button style="margin-right: 3px" size="mini"
                       type="danger" @click="deleteEmp(scope.row)">删除
            </el-button>
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
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible"
        width="70%">
      <div>
        <el-form ref="empForm" :model="emp" :rules="rules">
          <el-row>
            <el-col :span="6">
              <el-form-item label="姓名:" prop="name">
                <el-input placeholder="请输入员工名..." size="small" prefix-icon="el-icon-edit"
                          v-model="emp.name" style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="性别:" prop="gender">
                <el-radio-group v-model="emp.gender" style="margin-top: 8px">
                  <el-radio label="男">男</el-radio>
                  <el-radio label="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出生日期:" prop="birthday">
                <el-date-picker
                    v-model="emp.birthday"
                    type="date"
                    value-format="yyyy-MM-dd"
                    style="width: 150px"
                    placeholder="出生日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="政治面貌" prop="politicId">
                <el-select v-model="emp.politicId" placeholder="请选择政治面貌..." style="width: 200px">
                  <el-option
                      v-for="item in politicsStatus"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="民族:" prop="nationId">
                <el-select v-model="emp.nationId" placeholder="请选择民族..." style="width: 150px">
                  <el-option
                      v-for="item in nations"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="籍贯:" prop="nativePlace">
                <el-input placeholder="请输入籍贯..." size="small" prefix-icon="el-icon-edit"
                          v-model="emp.nativePlace" style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电子邮箱:" prop="email">
                <el-input placeholder="请输入邮箱..." size="small" prefix-icon="el-icon-message"
                          v-model="emp.email" style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="联系地址:" prop="address">
                <el-input placeholder="请输入联系地址..." size="small" prefix-icon="el-icon-edit"
                          v-model="emp.address" style="width: 200px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="职称:" prop="jobLevelId">
                <el-select v-model="emp.jobLevelId" placeholder="请选择职称..." style="width: 150px">
                  <el-option
                      v-for="item in joblevels"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="职位:" prop="posId">
                <el-select v-model="emp.posId" placeholder="请选择职位..." style="width: 150px">
                  <el-option
                      v-for="item in positions"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所属部门:" prop="departmentId">
                <el-popover
                    placement="bottom"
                    title="选择部门"
                    width="200"
                    trigger="manual"
                    v-model="visible">
                  <el-tree
                      :data="allDepartment"
                      :props="defaultProps"
                      @node-click="handleNodeClick"
                      default-expand-all
                      ref="tree">
                  </el-tree>
                  <div slot="reference" class="selectedDep" @click="showDepView">
                    {{ this.depName }}
                  </div>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="手机号码:" prop="phone">
                <el-input placeholder="请输入手机号码..." size="small" prefix-icon="el-icon-phone"
                          v-model="emp.phone" style="width: 200px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="工号:" prop="workID">
                <el-input size="small" prefix-icon="el-icon-s-custom" disabled
                          v-model="emp.workID" style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="学历:" prop="tiptopDegree">
                <el-select v-model="emp.tiptopDegree" placeholder="请选择学历..." style="width: 150px">
                  <el-option
                      v-for="item in tiptopDegree"
                      :key="item"
                      :label="item"
                      :value="item">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="毕业院校:" prop="school">
                <el-input placeholder="请输入院校..." size="small" prefix-icon="el-icon-edit"
                          v-model="emp.school" style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="专业名称:" prop="specialty">
                <el-input placeholder="请输入专业..." size="small" prefix-icon="el-icon-edit"
                          v-model="emp.specialty" style="width: 200px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="入职日期:" prop="beginDate">
                <el-date-picker
                    v-model="emp.beginDate"
                    type="date"
                    style="width: 125px"
                    value-format="yyyy-MM-dd"
                    placeholder="入职日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="转正日期:" prop="conversionTime">
                <el-date-picker
                    v-model="emp.conversionTime"
                    type="date"
                    style="width: 125px"
                    value-format="yyyy-MM-dd"
                    placeholder="转正日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="合同起始日期:" prop="beginContract">
                <el-date-picker
                    v-model="emp.beginContract"
                    type="date"
                    style="width: 125px"
                    value-format="yyyy-MM-dd"
                    placeholder="合同起始日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="合同终止日期:" prop="endContract">
                <el-date-picker
                    v-model="emp.endContract"
                    type="date"
                    style="width: 200px"
                    value-format="yyyy-MM-dd"
                    placeholder="合同终止日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="身份证号:" prop="idCard">
                <el-input placeholder="请输入身份证号..." size="small" prefix-icon="el-icon-edit"
                          v-model="emp.idCard" style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="聘用形式:" prop="engageForm">
                <el-radio-group v-model="emp.engageForm" style="margin-top: 8px">
                  <el-radio label="劳动合同">劳动合同</el-radio>
                  <el-radio label="劳务合同">劳务合同</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="婚姻状况:" prop="wedlock">
                <el-radio-group v-model="emp.wedlock" style="margin-top: 8px">
                  <el-radio label="已婚">已婚</el-radio>
                  <el-radio label="未婚">未婚</el-radio>
                  <el-radio label="离异">离异</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addEmployee">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "EmpBasic",
  data() {
    return {
      searchValue:{
        politicId: null,
        nationId: null,
        posId: null,
        jobLevelId: null,
        engageForm: '',
        departmentId: null,
        beginDateScope: null
      },
      showAdavancedDisabled: false,
      headers: {
        Authorization: window.sessionStorage.getItem('tokenStr')
      },
      updateDataDisabled: false,
      uploadFileText: '导入文件',
      uploadFileIcon: 'el-icon-upload2',
      title: '',
      visible: false,
      visible2: false,
      employees: [],
      loading: false,
      total: 0,
      currentPage: 1,
      size: 10,
      empName: '',
      dialogVisible: false,
      emp: {
        id: null,
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: null,
        nativePlace: '',
        politicId: null,
        email: '',
        phone: '',
        address: '',
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '在职',
        workID: '',
        contractTerm: null,
        conversionTime: '',
        beginContract: '',
        endContract: '',
        workAge: null,
        salaryId: null
      },
      politicsStatus: [],
      nations: [],
      joblevels: [],
      positions: [],
      tiptopDegree: [
        '博士', '硕士', '本科', '大专', '高中', '初中', '小学', '其他'
      ],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      allDepartment: [],
      depName: '请选择部门...',
      rules: {
        name: [{required: true, message: '请输入员工姓名', trigger: 'blur'}],
        gender: [{required: true, message: '请选择员工性别', trigger: 'blur'}],
        birthday: [{required: true, message: '请选择员工出生日期', trigger: 'blur'}],
        idCard: [
          {required: true, message: '请输入员工身份证号', trigger: 'blur'},
          {
            pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
            message: '请输入正确的身份证号', trigger: 'blur'
          }
        ],
        nationId: [{required: true, message: '请选择员工民族', trigger: 'blur'}],
        nativePlace: [{required: true, message: '请输入员工籍贯', trigger: 'blur'}],
        politicId: [{required: true, message: '请选择员工政治面貌', trigger: 'blur'}],
        email: [
          {required: true, message: '请输入员工邮箱', trigger: 'blur'},
          {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
        ],
        phone: [
          {required: true, message: '请输入员工手机号码', trigger: 'blur'},
          {pattern: /^[1]([3-9])[0-9]{9}$/, message: '请输入正确手机号码', trigger: 'blur'}
        ],
        address: [{required: true, message: '请输入员工联系地址', trigger: 'blur'}],
        departmentId: [{required: true, message: '请选择员工部门', trigger: 'blur'}],
        jobLevelId: [{required: true, message: '请选择员工职称', trigger: 'blur'}],
        posId: [{required: true, message: '请选择员工职位', trigger: 'blur'}],
        engageForm: [{required: true, message: '请选择员工聘用形式', trigger: 'blur'}],
        tiptopDegree: [{required: true, message: '请选择员工学历', trigger: 'blur'}],
        specialty: [{required: true, message: '请输入员工专业', trigger: 'blur'}],
        school: [{required: true, message: '请输入员工学校', trigger: 'blur'}],
        beginDate: [{required: true, message: '请选择员工入职日期', trigger: 'blur'}],
        workState: [{required: true, message: '请输入员工在职状态', trigger: 'blur'}],
        wedlock: [{required: true, message: '请选择员工婚姻状态', trigger: 'blur'}],
        conversionTime: [{required: true, message: '请选择员工转正日期', trigger: 'blur'}],
        beginContract: [{required: true, message: '请选择员工合同起始日期', trigger: 'blur'}],
        endContract: [{required: true, message: '请选择员工合同终止日期', trigger: 'blur'}],
      }
    }
  },
  mounted() {
    this.initEmployees();
    this.initData();
    this.initPostions();
  },
  methods: {
    //高级搜索部门
    searchHandleNodeClick(data){
      this.depName = data.name
      this.searchValue.departmentId = data.id;
      this.visible2 = !this.visible2;
    },
    //高级搜索
    advancedSearch(){
      console.log(this.searchValue)
    },
    //上传失败样式更改
    uploadDefeated() {
      this.uploadFileText = '导入文件';
      this.uploadFileIcon = 'el-icon-upload2';
      this.updateDataDisabled = false;
      this.initEmployees();
    },
    //上传成功样式更改
    uploadSuccess() {
      this.uploadFileText = '导入文件';
      this.uploadFileIcon = 'el-icon-upload2';
      this.updateDataDisabled = false;
    },
    //上传文件样式更改
    uploadFileStyle() {
      this.uploadFileText = '正在导入';
      this.uploadFileIcon = 'el-icon-loading';
      this.updateDataDisabled = true;
    },
    //导出员工数据
    exportEmployee() {
      this.downloadRequest('/system/basic/employee/export');
    },
    //查看编辑员工视图
    compileEmp(data) {
      this.title = '编辑员工信息';
      this.emp = data;
      this.depName = data.department.name;
      this.initPostions();
      this.dialogVisible = true;
    },
    //删除员工
    deleteEmp(data) {
      this.$confirm('此操作将永久删除[' + data.name + ']该员工, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/basic/employee/' + data.id).then(resp => {
          if (resp) {
            this.initEmployees();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //添加员工
    addEmployee() {
      if (this.emp.id == null) {
        this.$refs.empForm.validate((valid) => {
          if (valid) {

            this.postRequest('/system/basic/employee/', this.emp).then(resp => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmployees();
              }
            })
          }
        })
      } else {
        this.$refs.empForm.validate((valid) => {
          if (valid) {
            this.putRequest('/system/basic/employee/', this.emp).then(resp => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmployees();
              }
            })
          }
        })
      }
    },
    //节点点击事件
    handleNodeClick(data) {
      this.depName = data.name;
      this.emp.departmentId = data.id;
      this.visible = !this.visible;
    },
    //查询部门视图
    showDepView2() {
      this.visible2 = !this.visible2;
    },
    showDepView() {
      this.visible = !this.visible;
    },
    //点击添加视图
    showEmpView() {
      this.title = '添加员工';
      this.dialogVisible = true;
      this.initWorkId();
      this.emp = {
        id: null,
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: null,
        nativePlace: '',
        politicId: null,
        email: '',
        phone: '',
        address: '',
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '在职',
        workID: '',
        contractTerm: null,
        conversionTime: '',
        beginContract: '',
        endContract: '',
        workAge: null,
        salaryId: null
      };
      this.depName = '请选择部门...';
    },
    //根据用户名查询用户
    selectEmpName() {
      this.initEmployees();
    },
    //点击查询条数发生改变
    sizeEmpPage(size) {
      this.size = size;
      this.initEmployees('advanced');
    },
    //点击页数发生改变
    currentEmpPage(currentPage) {
      this.currentPage = currentPage;
      this.initEmployees('advanced');
    },
    initData() {
      //将数据存放到session中
      if (!window.sessionStorage.getItem("nations")) {
        //获取所有民族
        this.getRequest('/system/basic/employee/nation').then(resp => {
          if (resp) {
            this.nations = resp;
            window.sessionStorage.setItem("nations", JSON.stringify(resp));
          }
        })
      } else {
        this.nations = JSON.parse(window.sessionStorage.getItem('nations'));
      }

      if (!window.sessionStorage.getItem("joblevels")) {
        //获取所有职称
        this.getRequest('/system/basic/employee/joblevel').then(resp => {
          if (resp) {
            this.joblevels = resp;
            window.sessionStorage.setItem("joblevels", JSON.stringify(resp));
          }
        })
      } else {
        this.joblevels = JSON.parse(window.sessionStorage.getItem('joblevels'));
      }
      if (!window.sessionStorage.getItem("politicsStatus")) {
        //获取所有政治面貌
        this.getRequest('/system/basic/employee/politicsStatus').then(resp => {
          if (resp) {
            this.politicsStatus = resp;
            window.sessionStorage.setItem("politicsStatus", JSON.stringify(resp));
          }
        })
      } else {
        this.politicsStatus = JSON.parse(window.sessionStorage.getItem('politicsStatus'));
      }
      if (!window.sessionStorage.getItem("allDepartment")) {
        //初始化部门信息
        this.getRequest('/system/basic/department/').then(resp => {
          if (resp) {
            this.allDepartment = resp;
          }
        })
      } else {
        this.allDepartment = JSON.parse(window.sessionStorage.getItem('allDepartment'));
      }
    },
    //获取工号
    initWorkId() {
      if (this.emp.workID == '') {
        this.getRequest('/system/basic/employee/maxWorkId').then(resp => {
          if (resp) {
            this.emp.workID = resp.object;
          }
        })
      }
    },
    //获取所有职位
    initPostions() {
      this.getRequest('/system/basic/employee/position').then(resp => {
        if (resp) {
          this.positions = resp;
        }
      })
    },
    //获取所有员工
    initEmployees(type) {
      this.loading = true;
      let url = "/system/basic/employee/?currentPage=" + this.currentPage + "&size=" + this.size;

      if (type && type == 'advanced'){
        if (this.searchValue.politicId){
          url += "&politicId=" + this.searchValue.politicId
        }
        if (this.searchValue.nationId){
          url += '&nationId=' + this.searchValue.nationId
        }
        if (this.searchValue.posId){
          url += '&posId=' + this.searchValue.posId
        }
        if (this.searchValue.jobLevelId){
          url += '&jobLevelId=' + this.searchValue.jobLevelId
        }
        if (this.searchValue.engageForm){
          url += '&engageForm=' + this.searchValue.engageForm
        }
        if (this.searchValue.departmentId){
          url += '&departmentId=' + this.searchValue.departmentId
        }
        if (this.searchValue.beginDateScope){
          url += '&beginDateScope=' + this.searchValue.beginDateScope
        }
      }else {
        url +=  "&name=" + this.empName
      }

      this.getRequest(url).then(resp => {
        this.loading = false;
        this.depName = '请选择部门...'
        if (resp) {
          this.total = resp.total;
          this.employees = resp.data;
        }
      })
    }
  }
}
</script>

<style>
.selectedDep {
  width: 140px;
  display: inline-flex;
  border: 1px solid #dedede;
  height: 30px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 10px;
  padding-left: 5px;
  /*color: rgba(0, 0, 0, 0.38)*/
}
.selectedDepSearch{
  width: 130px;
  display: inline-flex;
  border: 1px solid #dedede;
  height: 23px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 10px;
  padding-top: 8px;
  padding-left: 5px;
}
.advancedSearch{
  border: 1px solid #5ec8ff;
  border-radius: 5px;
  box-sizing: border-box;
  padding: 6px;
  margin: 10px 0px;
}
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all .6s ease;
}
.slide-fade-leave-active {
  transition: all .6s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slide-fade-enter, .slide-fade-leave-to{
  transform: translateX(10px);
  opacity: 0;
}
</style>