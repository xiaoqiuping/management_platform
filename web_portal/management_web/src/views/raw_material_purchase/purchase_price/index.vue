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
        </el-form>
      </div>
    </el-card>
    <!--操作栏-->
    <div class="operate-container">
      <el-button size="primary" class="btn-add" @click="handleAdd()"><i class="el-icon-plus left_icon"></i>添加
      </el-button>
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
        <el-table-column label="价格(元)" align="center">
          <template slot-scope="scope">{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column label="用途" align="center">
          <template slot-scope="scope">{{ scope.row.purpose }}</template>
        </el-table-column>
        <el-table-column label="供应商" align="center">
          <template slot-scope="scope">{{ scope.row.supplier }}</template>
        </el-table-column>
        <el-table-column label="采购人" align="center">
          <template slot-scope="scope">{{ scope.row.purchase_man }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{ scope.row.remarks }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleUpdate(scope.$index, scope.row)">
              编辑
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
      :title="isEdit?'编辑采购价目':'新增采购价目'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="物料名称：" class="marginBottom22" prop="material_name">
          <el-input v-model="mainEntry.material_name" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="价格(元)：" class="marginBottom22" prop="price">
          <el-input v-model.number="mainEntry.price" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="用途：" class="marginBottom22">
          <el-input v-model="mainEntry.purpose"
                    type="textarea"
                    maxlength="200"
                    :rows="4"
                    show-word-limit
                    class="width450"></el-input>
        </el-form-item>
        <el-form-item label="供应商：" class="marginBottom22">
          <el-input v-model="mainEntry.supplier" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="采购人：" class="marginBottom22">
          <el-input v-model="mainEntry.purchase_man" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="备注：" class="marginBottom22">
          <el-input v-model="mainEntry.remarks"
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    class="width450"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose('form')" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm('form')" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listPage, save, remove, update} from '@/api/raw_materials_purchase/purchasePrice'
const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  material_name:null,
}
const defaultEntry = {
  id:null,
  material_name:null,
  price:null,
  purpose:null,
  supplier:null,
  purchase_man:null,
  remarks:null,
};
export default {
  name: "PurchasePrice",
  data(){
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      rules: {
        material_name: [
          {required: true, message: '物料名称不能为空'},
        ],
        price: [
          {required: true, message: '价格不能为空'},
          {type: 'number', message: '价格必须为数字值'}
        ],
      }
    }
  },
  created() {
    this.getList();
  },
  methods:{
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
      listPage(this.param,this.page).then(response => {
        this.listLoading = false;
        this.list = response.data.content;
        this.page.total = response.data.totalElements;
      });
    },
    handleReset(){
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
    handleClose(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
    },
    //保存或更新信息
    handleDialogConfirm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
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
        }
      })
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该采购价目?', '提示', {
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
