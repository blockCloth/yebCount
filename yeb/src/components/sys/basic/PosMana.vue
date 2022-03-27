<template>
  <div>
    <div>
      <el-input
          size="small"
          placeholder="添加职位..."
          suffix-icon="el-icon-circle-plus"
          @keydown.enter.native="addPostion"
          v-model="postion.name" class="addPostion">
      </el-input>
      <el-button type="primary" round size="small" icon="el-icon-circle-plus" @click="addPostion">添加职位</el-button>
    </div>
    <div>
      <el-table class="tablePostion"
                :data="postions"
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
            label="职位名称"
            width="130">
        </el-table-column>
        <el-table-column
            prop="createDate"
            label="创建日期"
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
               :disabled="this.multipleSelection.length==0" @click="deleteMany">批量删除</el-button>
    <el-dialog
        title="编辑职位"
        :visible.sync="dialogVisible"
        width="30%">
      <div>
        <el-tag>职位名称</el-tag>
        <el-input v-model="updatePos.name" size="small" class="updateIptPostion"></el-input>
        <br>
        <el-tag>是否启用</el-tag>
        <el-switch
            v-model="updatePos.enabled"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="已启用"
            inactive-text="未启用" class="updateIptPostion">
        </el-switch>
      </div>
      <span slot="footer" class="dialog-footer">
      <el-button size="small" @click="dialogVisible = false">取 消</el-button>
      <el-button size="small" type="primary" @click="updatePostion">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "PosMana",
  data() {
    return {
      postion: {
        name: ''
      },
      updatePos: {
        name: '',
        enabled: false
      },
      dialogVisible: false,
      postions: [],
      multipleSelection: []
    }
  },
  mounted() {
    return this.initPostion();
  },
  methods: {
    indexMethod(index){
      return index+1;
    },
    deleteMany(){
      this.$confirm('此操作将永久删除[' + this.multipleSelection.length + ']条职位？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //获取id值
        let ids = "?";
        this.multipleSelection.forEach(item=>{
          ids += "ids="+item.id+"&";
        })
        this.deleteRequest("/system/basic/pot" + ids).then(resp=>{
          if (resp) {
            this.initPostion();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    updatePostion(){
      //修改职位信息
      this.putRequest("/system/basic/pot/",this.updatePos).then(resp=>{
        if (resp){
          this.initPostion();
          this.dialogVisible = false;
        }
      })
    },
    addPostion() {
      if (this.postion.name) {
        this.postRequest("/system/basic/pot/", this.postion).then(resp => {
          if (resp) {
            this.initPostion();
            this.postion.name = '';
          }
        })
      } else {
        this.$message.error('职位名字不能为空！');
      }
    },
    showEditView(index,data){
      this.dialogVisible = true;
      this.updatePos.createDate = '';
      Object.assign(this.updatePos,data);
    },
    handleDelete(index, data) {
      this.$confirm('此操作将永久删除[' + data.name + ']职位？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/basic/pot/' + data.id).then(resp => {
          //判断数据是否刷新成功，成功则重新刷新数据
          if (resp) {
            this.initPostion();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },

    initPostion() {
      this.getRequest("/system/basic/pot/").then((resp) => {
        if (resp) {
          this.postions = resp;
        }
      })
    }
  }
}
</script>

<style>
.addPostion {
  width: 300px;
  margin-right: 10px;
}

.tablePostion {
  margin-top: 10px;
}
.updateIptPostion{
  width: 250px;
  margin-left: 10px;
}
</style>