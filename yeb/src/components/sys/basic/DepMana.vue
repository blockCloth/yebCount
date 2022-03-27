<template>
  <div>
    <div>
      <el-input
          size="small"
          placeholder="通过部门名称进行搜索..."
          v-model="filterText" class="selectedDep">
      </el-input>
    </div>
    <div style="width: 500px">
      <el-tree
          :data="department"
          :props="defaultProps"
          :filter-node-method="filterNode"
          :expand-on-click-node="false"
          ref="tree">
        <span class="custom-tree-node" slot-scope="{ node, data }"
              style="display: flex;justify-content: space-between;width: 100%">
            <span>{{ node.label }}</span>
            <span>
              <el-button
                  type="primary"
                  size="mini"
                  class="depBtn"
                  @click="showEditView(data)">
                添加部门
              </el-button>
              <el-button
                  type="danger"
                  size="mini"
                  class="depBtn"
                  @click="deleteDep(node, data)">
                删除部门
              </el-button>
            </span>
      </span>
      </el-tree>
    </div>
    <el-dialog
        title="添加部门"
        :visible.sync="dialogVisible"
        width="30%">
      <div>
        <el-tag>上级部门</el-tag>
        <el-input v-model="parentName" size="small" style="width: 250px;margin-left: 10px;" :disabled="true"></el-input>
        <br>
        <el-tag>部门名称</el-tag>
        <el-input v-model="updateDep.name" size="small" style="width: 250px;margin-left: 10px;margin-top: 5px"></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addDepartment">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  name: "DepMana",
  data() {
    return {
      filterText: '',
      department: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      dialogVisible: false,
      updateDep:{
        name: '',
        parentId: -1
      },
      parentName: ''
    }
  },
  mounted() {
    this.initDepartment();
  },
  methods: {
    initShow(){
      this.updateDep.name = '';
      this.updateDep.parentId = -1;
      this.parentName = '';
    },
    addDepartmentTwo(deps,dep){
      for (let i = 0; i < deps.length; i++) {
          let d = deps[i];
          if (d.id == dep.parentId){
            d.children = d.children.concat(dep);
          }else {
            this.addDepartmentTwo(d.children,dep);
          }
      }
    },
    //添加部门
    addDepartment(){
      //发送请求
      this.postRequest('/system/basic/department/',this.updateDep).then(resp=>{
        if (resp){
          this.addDepartmentTwo(this.department,resp.object);
          this.dialogVisible = false;
          this.initShow();
        }
      })
    },
    //删除部门并显示树形结构
    removeShowTree(deps,id){
      for (let i = 0; i < deps.length; i++) {
          let d = deps[i];
          if (d.id == id){
            deps.splice(i,1);
            return;
          }else {
            this.removeShowTree(d.children,id);
          }
      }
    },
    //删除部门
    deleteDep(node, data){
      this.$confirm('此操作将永久删除['+data.name+']部门！, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/basic/department/'+data.id).then(resp=>{
          if (resp){
            this.removeShowTree(this.department,data.id);
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //添加部门
    showEditView(data) {
      //获取到父部门名称
      this.parentName = data.name;
      this.dialogVisible = true;
      //获取父部门id以及新部门名称
      this.updateDep.parentId = data.id;
    },
    //拦截节点
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    //初始化部门信息
    initDepartment() {
      this.getRequest('/system/basic/department/').then(resp => {
        if (resp) {
          this.department = resp;
        }
      })
    }
  }
}
</script>

<style>
.selectedDep {
  width: 500px;
  padding-right: 8px;
}

.depBtn {
  padding: 2px;
}
</style>