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
            <el-input v-model="param.material_name" class="input-width" placeholder="物料名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.material_model" class="input-width" placeholder="物料型号" clearable></el-input>
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
        <el-table-column label="物料名称" align="center">
          <template slot-scope="scope">{{ scope.row.material_name }}</template>
        </el-table-column>
        <el-table-column label="物料型号" align="center">
          <template slot-scope="scope">{{ scope.row.material_model |materialModelFilter }}</template>
        </el-table-column>
        <el-table-column label="物料类别" align="center">
          <template slot-scope="scope">{{ scope.row.material_type | materialTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="子类别" align="center">
          <template slot-scope="scope">{{ scope.row | childrenTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="库存量" align="center">
          <template slot-scope="scope">{{ scope.row.total_num }}</template>
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
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {listAllWithOutRawMaterial} from '@/api/warehouse_storage/material'
import {materialType} from "@/const/materialType"

const defaultPage = {
  page: 1,
  size: 10,
}
const defaultParam = {
  material_name: null,
  material_model: null,
  material_type: null,
  children_type: null,
}
const defaultEntry = {
  id: null,
  material_name: null,
  material_model: null,
  material_type: null,
  children_type: null,
  total_num: 0,
};
export default {
  name: "Material",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      total: 0,
      materialType,
    }
  },
  created() {
    this.getList();
  },
  filters: {
    materialTypeFilter(type) {
      return materialType.filter(v => v.value === type)[0].label
    },
    childrenTypeFilter(type) {
      let m = materialType.filter(v => v.value === type.material_type)[0];
      if (m.children) {
        return m.children.filter(c => c.value === type.children_type)[0].label
      } else {
        return "N/A"
      }
    },
    materialModelFilter(type){
      return type?type:"N/A"
    }
  },
  methods: {
    handleResetSearch() {
      this.page = Object.assign({}, defaultPage);
      this.param = Object.assign({}, defaultParam);
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
    handleCancel() {
      this.dialogVisible = false;
    },
    //查询
    getList() {
      this.listLoading = true;
      listAllWithOutRawMaterial(this.page).then(response => {
        this.listLoading = false;
        this.list = response.data.content;
        this.total = response.data.totalElements;
      });
    },
    handleReset() {
      this.getList();
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>

