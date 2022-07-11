<template>
  <div class="app-container">
    <!--搜索栏-->
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
      <div>
        <el-form :inline="true" :model="param" size="small" label-width="140px">
          <el-form-item>
            <el-input v-model="param.order_no" class="input-width" placeholder="单号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.watch_name" class="input-width" placeholder="手表名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.watch_type" class="input-width" placeholder="手表型号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.buyer" class="input-width" placeholder="购买商" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.salesman" class="input-width" placeholder="销售人" clearable></el-input>
          </el-form-item>
          <el-form-item style="margin-bottom: 10px">
            <el-date-picker
              v-model="sale_date"
              type="daterange"
              align="right"
              value-format="yyyy-MM-dd"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <!--操作栏-->
    <div class="operate-container">
      <el-button class="btn-add" @click="handleReset()"><i class="el-icon-refresh left_icon"></i>刷新
      </el-button>
    </div>
    <!--表格-->
    <div class="table-container">
      <el-table ref="adminTable"
                :data="list"
                style="width: 100%;"
                v-loading="listLoading" >
        <el-table-column label="单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="手表名称" align="center">
          <template slot-scope="scope">{{ scope.row.watch_name}}</template>
        </el-table-column>
        <el-table-column label="手表型号" align="center">
          <template slot-scope="scope">{{ scope.row.watch_type}}</template>
        </el-table-column>
        <el-table-column label="销售数量" align="center">
          <template slot-scope="scope">{{ scope.row.sale_num }}</template>
        </el-table-column>
        <el-table-column label="成交价格" align="center">
          <template slot-scope="scope">{{ scope.row.tran_price }}</template>
        </el-table-column>
        <el-table-column label="购买商" align="center">
          <template slot-scope="scope">{{ scope.row.buyer }}</template>
        </el-table-column>
        <el-table-column label="销售人" align="center">
          <template slot-scope="scope">{{ scope.row.salesman }}</template>
        </el-table-column>
        <el-table-column label="销售时间" align="center">
          <template slot-scope="scope">{{ scope.row.sale_date }}</template>
        </el-table-column>
        <el-table-column label="销售状态" align="center">
          <template slot-scope="scope">退货</template>
        </el-table-column>
      </el-table>
    </div>
    <!--搜索页面-->
    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="total, sizes,prev, pager, next,jumper"
        :current-page.sync="page.page"
        :page-size="page.size"
        :page-sizes="[10,15,20]"
        :total="page.total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {listPage} from '@/api/sale/order'
const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  order_no:null,
  watch_name:null,
  watch_type:null,
  buyer:null,
  salesman:null,
  sale_start_date:null,
  sale_end_date:null,
  status_list:[4],
}
const defaultEntry = {
  id:null,
  order_no:null,
  watch_name:null,
  watch_type:null,
  sale_num:null,
  tran_price:null,
  buyer:null,
  salesman:null,
  sale_date:null,
  status:null,
  change_flag:null,
}
export default {
  name: "ReturnNoticeList",
  data(){
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      sale_date:null
    }
  },
  created() {
    this.getList();
  },
  methods:{
    handleResetSearch() {
      this.page = Object.assign({}, defaultPage);
      this.param = Object.assign({}, defaultParam);
      this.sale_date=null;
    },
    handleSearchList() {
      this.page.page = 1;
      this.getList();
    },
    handleSizeChange(val) {
      this.page.page = 1;
      this.page.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.page.page = val;
      this.getList();
    },
    handleReset(){
      this.getList();
    },
    //查询
    getList() {
      this.listLoading = true;
      if(this.sale_date){
        this.param.sale_start_date=this.sale_date[0]
        this.param.sale_end_date=this.sale_date[1]
      }else {
        this.param.sale_start_date=null;
        this.param.sale_end_date=null;
      }
      listPage(this.param,this.page).then(response => {
        this.listLoading = false;
        this.list = response.data.content;
        this.page.total = response.data.totalElements;
      });
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>
