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
            <el-input v-model="param.goods_name" class="input-width" placeholder="商品名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.buyer" class="input-width" placeholder="购买商" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.type" clearable placeholder="请选择类别" @clear="param.type=null">
              <el-option
                v-for="type in typeEnums"
                :key="type.type"
                :label="type.desc"
                :value="type.type">
              </el-option>
            </el-select>
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
        <el-table-column label="商品名称 / 购买商" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.type===0">{{ scope.row.goods_name}}</span>
            <span v-if="scope.row.type===1">{{ scope.row.buyer}}</span>
          </template>
        </el-table-column>
        <el-table-column label="折扣价(元)" align="center">
          <template slot-scope="scope">{{ scope.row.price }}</template>
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
      :title="isEdit?'编辑销售折扣信息':'新增销售折扣信息'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="类别" class="marginBottom22" prop="type">
          <el-select v-model="mainEntry.type" clearable placeholder="请选择类别" @clear="param.type=null">
            <el-option class="width450"
              v-for="type in typeEnums"
              :key="type.type"
              :label="type.desc"
              :value="type.type">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="购买商：" class="marginBottom22" v-if="mainEntry.type===1" prop="buyer">
          <el-input v-model="mainEntry.buyer" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="商品名称：" class="marginBottom22" v-if="mainEntry.type===0" prop="goods_name">
          <el-input v-model="mainEntry.goods_name" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="折扣价(元)：" class="marginBottom22" prop="price">
          <el-input v-model.number="mainEntry.price" class="width450"></el-input>
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
import {listPage, save, remove, update} from '@/api/sale/saleOffPrice'
const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  goods_name:null,
  goods_type:null,
  type:null,
}
const defaultEntry = {
  id:null,
  goods_name:null,
  buyer:null,
  price:null,
  type:null,
};
const typeEnums = [
  {
    type: 0,
    desc: "商品名称"
  },
  {
    type: 1,
    desc: "购买商"
  },
]
export default {
  name: "SaleoffPrice",
  data(){
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      typeEnums,
      rules: {
        type: [
          {required: true, message: '请选择类别',trigger: 'change'},
        ],
        buyer: [
          {required: true, message: '购买商不能为空'},
        ],
        goods_name: [
          {required: true, message: '商品名称不能为空'},
        ],
        price: [
          {required: true, message: '折扣价不能为空'},
          {type: 'number', message: '折扣价必须为数字值'}
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
      this.$confirm('是否要删除该销售折扣信息?', '提示', {
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
