<template>
  <div class="pagination-container">
    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      layout="total, sizes,prev, pager, next,jumper"
      :current-page.sync="page.page"
      :page-size="page.size"
      :page-sizes="[10,15,20]"
      :total="total">
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: "PaginationContainer",
  props:[{
    'page':{
      type:Object,
      default:{
        page:1,
        size:10
      }
    },
    'total':{
      type:Number,
      default: 0
    },
    sizeChange :{},
    currentChange : {}
  }],
  methods:{
    handleSizeChange(){
      sizeChange()
    },
    handleCurrentChange(){
      currentChange()
    }
  }
}
</script>

<style scoped>

</style>
