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
            <el-input v-model="param.cg_order_no" class="input-width" placeholder="采购单号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.raw_material_name" class="input-width" placeholder="原料名称" clearable></el-input>
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
                v-loading="listLoading">
        <el-table-column label="采购单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.cg_order_no }}</template>
        </el-table-column>
        <el-table-column label="原料名称" align="center">
          <template slot-scope="scope">{{ scope.row.raw_material_name }}</template>
        </el-table-column>
        <el-table-column label="数量" align="center">
          <template slot-scope="scope">{{ scope.row.total_num }}</template>
        </el-table-column>
        <el-table-column label="采购人" align="center">
          <template slot-scope="scope">{{ scope.row.purchase_man }}</template>
        </el-table-column>
        <el-table-column label="质检人" align="center">
          <template slot-scope="scope">{{ scope.row.quality_man }}</template>
        </el-table-column>
        <el-table-column label="合格量" align="center">
          <template slot-scope="scope">{{ scope.row.qualified_num }}</template>
        </el-table-column>
        <el-table-column label="质检状态" align="center">
          <template slot-scope="scope">{{ scope.row.status | statusFilter }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{ scope.row.remarks }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini" v-if="!scope.row.status ||scope.row.status===2"
                       type="text"
                       @click="handleUpdate(scope.$index, scope.row)">
              质检
            </el-button>
            <el-button size="mini"
                       type="text"
                       @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
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
    <!--添加编辑页面-->
    <el-dialog
      :title="'填写质检信息'"
      :visible.sync="dialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="采购单号：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.cg_order_no" disabled style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="原料名称：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.raw_material_name" disabled style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="数量：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.total_num" disabled style="width: 350px"></el-input>
        </el-form-item>

        <el-form-item label="合格量：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.qualified_num" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="质检状态：" style="margin-bottom: 10px">
          <el-radio v-model="mainEntry.status" :label="1" border>审核通过</el-radio>
          <el-radio v-model="mainEntry.status" :label="2" border>审核未通过</el-radio>
        </el-form-item>
        <el-form-item label="备注：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.remarks"
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm()" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listPage, save, remove, update} from '@/api/quality_restriction/rawMaterialQuality'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  cg_order_no: null,
  raw_material_name: null,
}
const defaultEntry = {
  id: null,
  cg_order_no: null,
  raw_material_name: null,
  total_num: null,
  purchase_man: null,
  quality_man: null,
  qualified_num: null,
  status: null,
  remarks: null,

};
export default {
  name: "RawMaterialQuality",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
    }
  },
  created() {
    this.getList();
  },
  filters: {
    statusFilter(val) {
      if(!val){
        return "N/A"
      }
      return val === 1 ? "审核通过" : "审核不通过"
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
    //查询
    getList() {
      this.listLoading = true;
      listPage(this.param, this.page).then(response => {
        this.listLoading = false;
        this.list = response.data.content;
        this.page.total = response.data.totalElements;
      });
    },
    handleReset() {
      this.getList();
    },
    // 添加（唤起dialog）
    handleAdd() {
      this.dialogVisible = true;
      this.isEdit = false;
      this.mainEntry = Object.assign({}, defaultEntry);
    },
    //更新（唤起dialog）
    handleUpdate(index, row) {
      this.dialogVisible = true;
      this.isEdit = true;
      this.mainEntry = Object.assign({}, row);
    },
    //保存或更新信息
    handleDialogConfirm() {
      this.$confirm('是否确认保存?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.isEdit) {
          update(this.mainEntry).then(response => {
            this.$message({
              message: '修改成功！',
              type: 'success'
            });
            this.dialogVisible = false;
            this.getList();
          })
        } else {
          save(this.mainEntry).then(response => {
            this.$message({
              message: '添加成功！',
              type: 'success'
            });
            this.dialogVisible = false;
            this.getList();
          })
        }
      })
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该价目?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove(row.id).then(response => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.getList();
        });
      });
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>
