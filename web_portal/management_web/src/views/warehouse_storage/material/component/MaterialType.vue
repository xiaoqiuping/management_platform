<template>
  <div style="margin-top: 50px">
    <el-form  label-width="120px" style="width: 600px" size="small">
      <el-form-item label="物料类别：" >
        <el-cascader :options="materialType" v-model="chooseType" clearable></el-cascader>
      </el-form-item>
      <el-form-item style="text-align: center;margin:20px">
        <el-button type="primary" size="medium" @click="handleNext()">下一步，填写物料信息</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import {materialType} from "@/const/materialType"

export default {
  name: "MaterialType",
  data() {
    return {
      materialType,
      chooseType:null,
    }
  },
  methods:{
    handleNext(){
      this.$emit('nextStep',this.chooseType);
    }
  }
}
</script>
