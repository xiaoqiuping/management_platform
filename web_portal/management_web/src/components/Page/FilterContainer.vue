<template>
  <el-card class="filter-container" shadow="never" style="padding: 10px 5px">
    <div style="height: 40px">
      <el-button
        style="float:left"
        type="primary"
        @click="handleSearchList()"
        size="small">
        查询搜索<i class="el-icon-search right_icon"></i>
      </el-button>
      <el-button
        style="float:left;margin-left: 5px"
        @click="handleResetSearch()"
        size="small">
        重置
      </el-button>
    </div>
    <slot></slot>
  </el-card>
</template>
<script>
export default {
  name: 'filterContainer',
  props: {
    searchList :{},
    resetSearch : {}
  },
  data(){
    return {
    }
  },
  methods:{
    handleSearchList(){
      this.searchList()
    },
    handleResetSearch(){
      this.resetSearch()
    }
  }
}
</script>
<style>
.el-card__body {
  padding: 0;
  margin: 0;
}

.input_column {
  margin-bottom: 10px;
}

</style>
