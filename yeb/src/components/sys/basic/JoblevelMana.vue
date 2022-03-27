<template>
  <div>
    <div>
      <el-input
          size="small"
          placeholder="添加职称..."
          suffix-icon="el-icon-circle-plus"
          v-model="joblevel.name" style="width: 300px;margin-right: 10px;">
      </el-input>
      <el-select size="small" v-model="joblevel.titleLevel" placeholder="选择职称登记">
        <el-option
            v-for="item in titleLevels"
            :key="item"
            :label="item"
            :value="item">
        </el-option>
      </el-select>
      <el-button size="small" type="primary" icon="el-icon-circle-plus"
                 style="margin-left: 6px" @click="addJoblevel">添加职称
      </el-button>
    </div>
    <div>
      <el-table class="tablePostion"
                :data="joblevels"
                stripe
                border
                @selection-change="handleSelectionChange">
        style="width: 70%">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            label="编号"
            type="index"
            :index="indexMethod"
            width="60">
        </el-table-column>
        <el-table-column
            prop="name"
            label="职称名称"
            width="130">
        </el-table-column>
        <el-table-column
            prop="titleLevel"
            label="职称等级"
            width="130">
        </el-table-column>
        <el-table-column
            prop="createDate"
            label="创建时间"
            width="200">
        </el-table-column>
        <el-table-column
            prop="enable"
            label="是否启用"
            width="150">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
            <el-tag v-else type="danger">未启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="showEditView(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-button type="danger" size="small" style="margin-top: 9px"
               :disabled="this.multipleSelection.length==0" @click="deleteMany">批量删除
    </el-button>

    <el-dialog
        title="编辑职位"
        :visible.sync="dialogVisible"
        width="30%">
      <div>
        <el-tag>职称名称</el-tag>
        <el-input v-model="updateJoblevel.name" size="small" style="width: 240px;margin-left: 8px"></el-input>
        <br><br>
        <el-tag>职称登记</el-tag>
        <el-select size="small" v-model="updateJoblevel.titleLevel" placeholder="选择职称等级"
                   style="width: 240px;margin-left: 8px">
          <el-option
              v-for="item in titleLevels"
              :key="item"
              :label="item"
              :value="item">
          </el-option>
        </el-select>
        <br><br>
        <el-tag>是否启用</el-tag>
        <el-switch
            v-model="updateJoblevel.enabled"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="已启用"
            inactive-text="未启用" style="width: 240px;margin-left: 8px">
        </el-switch>
      </div>
      <span slot="footer" class="dialog-footer">
      <el-button size="small" @click="dialogVisible = false">取 消</el-button>
      <el-button size="small" type="primary" @click="updateJob">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "JoblevelMana",
  data() {
    return {
      joblevel: {
        name: '',
        titleLevel: ''
      },
      titleLevels: [
        '正高级',
        '副高级',
        '高级',
        '中级',
        '初级',
        '员级'
      ],
      joblevels: [],
      multipleSelection: [],
      dialogVisible: false,
      updateJoblevel:{
        name: '',
        titleLevel: '',
        enabled: ''
      }
    }
  },
  mounted() {
    this.initJoblevel();
  },
  methods: {
    //循环下标
    indexMethod(index){
      return index+1;
    },
    //修改职称
    updateJob(){
      this.putRequest("/system/basic/joblevel/",this.updateJoblevel).then(resp=>{
        if (resp){
          this.initJoblevel();

        }
      })
    },
    //编辑职称
    showEditView(index,data){
      this.dialogVisible = true;
      this.updateJoblevel.createDate = '';
      Object.assign(this.updateJoblevel,data);
    },
    //添加职称
    addJoblevel() {
      //判断名字和职称是否为空
      if (this.joblevel.name && this.joblevel.titleLevel) {
        this.postRequest('/system/basic/joblevel/', this.joblevel).then(resp => {
          if (resp) {
            this.initJoblevel();
          }
        })
      } else {
        this.$message.error("职称名字或者等级不能为空！！！");
      }
    },
    //获取职称信息
    initJoblevel() {
      this.getRequest('/system/basic/joblevel/').then(resp => {
        if (resp) {
          this.joblevels = resp;
          //重置职称信息
          this.joblevel.name = '';
          this.joblevel.titleLevel = '';
          this.dialogVisible = false;
        }
      })
    },
    //删除职称信息
    handleDelete(index, data) {
      this.$confirm('此操作将永久删除[' + data.name + ']职称？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/basic/joblevel/' + data.id).then(resp => {
          //判断数据是否刷新成功，成功则重新刷新数据
          if (resp) {
            this.initJoblevel();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //获取多选框
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //批量删除职称信息
    deleteMany() {
      this.$confirm('此操作将永久删除[' + this.multipleSelection.length + ']条职称？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //获取id值
        let ids = "?";
        this.multipleSelection.forEach(item => {
          ids += "ids=" + item.id + "&";
        })
        this.deleteRequest("/system/basic/joblevel/" + ids).then(resp => {
          if (resp) {
            this.initJoblevel();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    }
  }
}
</script>

<style scoped>

</style>