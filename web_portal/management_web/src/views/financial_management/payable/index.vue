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
            <el-select v-model="param.status" clearable placeholder="请选择状态" @clear="param.status=null">
              <el-option
                v-for="status in statusEnum"
                :key="status.value"
                :label="status.value"
                :value="status.type">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.payable_order_name" class="input-width" placeholder="应付单名称" clearable></el-input>
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
                v-loading="listLoading">
        <el-table-column label="应付单单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="应付单类别" align="center">
          <template slot-scope="scope">{{ scope.row.type|typeFilter }}</template>
        </el-table-column>
        <el-table-column label="应付单名称" align="center">
          <template slot-scope="scope">{{ scope.row.payable_order_name }}</template>
        </el-table-column>
        <el-table-column label="应付金额(元)" align="center">
          <template slot-scope="scope">{{ scope.row.total_amount }}</template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" width="170px">
          <template slot-scope="scope">{{ scope.row.create_time }}</template>
        </el-table-column>
        <el-table-column label="创建人" align="center">
          <template slot-scope="scope">{{ scope.row.duty_man }}</template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">{{ scope.row.status | statusFilter }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{ scope.row.remarks }}</template>
        </el-table-column>
        <el-table-column label="付款日期" align="center">
          <template slot-scope="scope">{{ scope.row.payable_date }}</template>
        </el-table-column>
        <el-table-column label="付款备注" align="center">
          <template slot-scope="scope">{{ scope.row.financial_remarks }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini" v-if="scope.row.status===0"
                       type="text"
                       @click="handleUpdate(scope.$index, scope.row)">
              付款
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
      :title="isEdit?'编辑应付单':'新增应付单'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="应付款类别" class="marginBottom22" prop="receive_money_type">
          <el-select v-model="mainEntry.type"
                     class="width450"
                     clearable placeholder="请选择应付款类别"
                     @clear="param.type=null">
            <el-option
              v-for="t in typeEnum"
              :key="t.type"
              :label="t.value"
              :value="t.type">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="应付单名称：" class="marginBottom22" prop="payable_order_name">
          <el-input v-model="mainEntry.payable_order_name" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="应付金额(元)：" class="marginBottom22" prop="total_amount">
          <el-input v-model.number="mainEntry.total_amount" class="width450"></el-input>
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
    <!--付款页面-->
    <el-dialog
      :title="'更新付款信息'"
      :visible.sync="fkDialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="付款日期：" style="margin-bottom: 10px">
          <el-date-picker style="width: 350px"
                          v-model="mainEntry.payable_date"
                          type="date"
                          align="right"
                          value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.financial_remarks"
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="fkDialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm()" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listPage, save, remove, update, updateStatus} from '@/api/raw_materials_purchase/payableOrder'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  payable_order_name: null,
  status: null,
}

const defaultEntry = {
  id: null,
  order_no: null,
  payable_order_name: null,
  type: null,
  status: null,
  create_time: null,
  total_amount: null,
  duty_man: null,
  rk_order_no: null,
  remarks: null,
  payable_date: null,
  financial_remarks: null
};
const statusEnum = [
  {
    type: 0,
    value: '未付'
  },
  {
    type: 1,
    value: '已付'
  }
]
const typeEnum = [
  {
    type: 1,
    value: '采购应付'
  },
  {
    type: 2,
    value: '办公耗材应付'
  }
]

export default {
  name: "PayableOrder",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      fkDialogVisible:false,
      isEdit: false,
      statusEnum,
      typeEnum,
      rules: {
        payable_order_name: [
          {required: true, message: '应付单名称不能为空'},
        ],
        total_amount: [
          {required: true, message: '应付金额不能为空'},
          {type: 'number', message: '应付金额必须为数字值'}
        ],
      }
    }
  },
  created() {
    this.getList();
  },
  filters: {
    statusFilter(status) {
      return status === 0 ? '未付' : '已付'
    },
    typeFilter(type) {
      return type === 1 ? '采购应付' : '办公耗材应付'
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
    handleClose(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
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
          this.mainEntry.status = 1
          update(this.mainEntry).then(response => {
            this.$message({
              message: '修改成功！',
              type: 'success'
            });
            this.dialogVisible = false;
            this.getList();
          })
        } else {
          this.mainEntry.status = 1


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
      this.$confirm('是否要删除该应付单?', '提示', {
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
    handlePayable(id, status) {
      this.$confirm('是否要更改应付单状态?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateStatus(id, status).then(response => {
          this.$message({
            type: 'success',
            message: '更新成功!'
          });
          this.getList();
        });
      });
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>
