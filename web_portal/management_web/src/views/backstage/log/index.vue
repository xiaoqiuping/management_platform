<template>
  <div class="app-container">
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
      <el-form :inline="true" size="small" label-width="140px">
        <el-form-item>
          <el-input v-model="param.account" class="input-width" placeholder="操作人" clearable @clear="param.account=null"></el-input>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="operation_time"
            type="datetimerange"
            :picker-options="pickerOptions"
            align="right"
            value-format="yyyy-MM-dd HH:mm:ss"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            :default-time="['00:00:00', '23:59:59']">
          </el-date-picker>
        </el-form-item>
      </el-form>
    </div>
  </el-card>

  <div class="table-container">
    <el-table ref="adminTable"
              :data="list"
              style="width: 100%;"
              v-loading="listLoading">
      <el-table-column label="操作人" align="center">
        <template slot-scope="scope">{{ scope.row.account }}</template>
      </el-table-column>
      <el-table-column label="操作时间" align="center">
        <template slot-scope="scope">{{ scope.row.operation_time}}</template>
      </el-table-column>
      <el-table-column label="操作内容" align="center">
        <template slot-scope="scope">{{ scope.row.log_operation}}</template>
      </el-table-column>
    </el-table>
  </div>

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

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  account: null,
  operation_start_time:null,
  operation_end_time:null,
}
import {listPage} from '@/api/backstage/log'
export default {
  name: "Log",
  data(){
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      list: null,
      operation_time:null,
      listLoading: false,
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleResetSearch() {
      this.page = Object.assign({}, defaultPage);
      this.param = Object.assign({}, defaultParam);
      this.operation_time = null
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
    //查询
    getList() {
      this.listLoading = true;
      if(this.operation_time){
        this.param.operation_start_time=this.operation_time[0]
        this.param.operation_end_time=this.operation_time[1]
      }else {
        this.param.operation_start_time=null;
        this.param.operation_end_time=null;
      }
      listPage(this.param, this.page).then(response => {
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
