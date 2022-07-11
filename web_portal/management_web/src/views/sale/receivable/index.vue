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
            <el-input v-model="param.duty_man" class="input-width" placeholder="责任人" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.receive_money_type" clearable placeholder="请选择应收款类别"
                       @clear="param.receive_money_type=null">
              <el-option
                v-for="receiveMoneyType in receiveMoneyTypeEnums"
                :key="receiveMoneyType.type"
                :label="receiveMoneyType.desc"
                :value="receiveMoneyType.type">
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
        <el-table-column label="单号" align="center">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="应收款类别" align="center">
          <template slot-scope="scope">{{ scope.row.receive_money_type | receiveMoneyTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="应收款" align="center">
          <template slot-scope="scope">{{ scope.row.receive_money }}</template>
        </el-table-column>
        <el-table-column label="责任人" align="center">
          <template slot-scope="scope">{{ scope.row.duty_man }}</template>
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
      :title="isEdit?'编辑应收款':'新增应收款'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="应收款类别" class="marginBottom22" prop="receive_money_type">
          <el-select v-model="mainEntry.receive_money_type"
                     class="width450"
                     clearable placeholder="请选择应收款类别"
                     @clear="param.receive_money_type=null">
            <el-option
              v-for="receiveMoneyType in receiveMoneyTypeEnums"
              :key="receiveMoneyType.type"
              :label="receiveMoneyType.desc"
              :value="receiveMoneyType.type">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="应收款：" class="marginBottom22" prop="receive_money">
          <el-input v-model.number="mainEntry.receive_money" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="责任人：" class="marginBottom22">
          <el-input v-model="mainEntry.duty_man" class="width450"></el-input>
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

import {listPage, save, remove, update} from '@/api/sale/receivable'
import change from "../order/change"

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  order_no: null,
  receive_money_type: null,
  duty_man: null,
  type: 1,
}
const defaultEntry = {
  id: null,
  order_no: null,
  receive_money: null,
  receive_money_type: null,
  duty_man: null,
  remarks: null,
  type: 1,
};

const receiveMoneyTypeEnums = [
  {
    type: 1,
    desc: "合同应收款"
  },
  {
    type: 2,
    desc: "零件加工款"
  },
  {
    type: 3,
    desc: "手表销售款"
  }
]


export default {
  name: "ReceivableList",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      receiveMoneyTypeEnums,
      rules: {
        receive_money_type: [
          {required: true, message: '请选择应收款类别', trigger: 'change'},
        ],
        receive_money: [
          {required: true, message: '应收款不能为空'},
          {type: 'number', message: '应收款必须为数字值'}
        ],
      }
    }
  },
  created() {
    this.getList();
  },
  filters: {
    receiveMoneyTypeFilter(type) {
      return receiveMoneyTypeEnums.filter(r => r.type === type)[0].desc;
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
      this.$confirm('是否要删除该应收单?', '提示', {
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
