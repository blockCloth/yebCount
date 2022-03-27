<template>
<div>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="部门管理" name="Depmana"><DepMana></DepMana></el-tab-pane>
    <el-tab-pane label="职位管理" name="Posmana"><PosMana></PosMana></el-tab-pane>
    <el-tab-pane label="职称管理" name="Joblevelmana"><JoblevelMana></JoblevelMana></el-tab-pane>
    <el-tab-pane label="奖惩管理" name="Ecname"><EcMana></EcMana></el-tab-pane>
    <el-tab-pane label="权限组" name="Permissmana"><PermissMana></PermissMana></el-tab-pane>
  </el-tabs>
</div>
</template>

<script>
import DepMana from "@/components/sys/basic/DepMana";
import PosMana from "@/components/sys/basic/PosMana";
import JoblevelMana from "@/components/sys/basic/JoblevelMana";
import EcMana from "@/components/sys/basic/EcMana";
import PermissMana from "@/components/sys/basic/PermissMana";

export default {

  name: "SysBasic",
  data(){
    return{
      activeName: 'Depmana'
    }
  },
  components:{
    DepMana,
    PosMana,
    JoblevelMana,
    EcMana,
    PermissMana
  }
}
</script>

<style scoped>

</style>