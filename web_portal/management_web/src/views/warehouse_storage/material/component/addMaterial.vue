<template>
  <el-card class="form-container" shadow="never" style="padding: 35px 35px 15px">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="选择物料类型"></el-step>
      <el-step title="填写物料基本信息"></el-step>
    </el-steps>
    <MaterialType
    v-show="showStatus[0]"
    @nextStep="nextStep">
    </MaterialType>
  </el-card>
</template>

<script>

import MaterialType from "./MaterialType"

export default {
  name: "addMaterial",
  components: {MaterialType},
  data(){
    return {
      active: 0,
      material_type:null,
      showStatus: [true, false],
    }
  },
  methods:{
    hideAll() {
      for (let i = 0; i < this.showStatus.length; i++) {
        this.showStatus[i] = false;
      }
    },
    prevStep() {
      if (this.active > 0 && this.active < this.showStatus.length) {
        this.active--;
        this.hideAll();
        this.showStatus[this.active] = true;
      }
    },
    nextStep(data) {
      if(this.active===0){
        this.material_type =data;
      }

      if (this.active < this.showStatus.length - 1) {
        this.active++;
        this.hideAll();
        this.showStatus[this.active] = true;
      }
      console.log(this.material_type)
    }
  }
}
</script>

<style scoped>

</style>
