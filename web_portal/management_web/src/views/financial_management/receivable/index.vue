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
          <!--          <el-form-item>-->
          <!--            <el-input v-model="param.duty_man" class="input-width" placeholder="责任人" clearable></el-input>-->
          <!--          </el-form-item>-->
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
                v-loading="listLoading">
        <el-table-column label="单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="应收款类型" align="center">
          <template slot-scope="scope">{{ scope.row.type | typeFilter }}</template>
        </el-table-column>
        <el-table-column label="应收款类别" align="center">
          <template slot-scope="scope">{{ scope.row.receive_money_type | receiveMoneyTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="应收金额(元)" align="center">
          <template slot-scope="scope">{{ scope.row.receive_money }}</template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" width="170px">
          <template slot-scope="scope">{{ scope.row.create_time }}</template>
        </el-table-column>
        <el-table-column label="责任人" align="center">
          <template slot-scope="scope">{{ scope.row.duty_man }}</template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">{{ scope.row.status | statusFilter }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{ scope.row.remarks }}</template>
        </el-table-column>
        <el-table-column label="收款日期" align="center">
          <template slot-scope="scope">{{ scope.row.receivable_date }}</template>
        </el-table-column>
        <el-table-column label="收款备注" align="center">
          <template slot-scope="scope">{{ scope.row.financial_remarks }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini" v-if="scope.row.status===0"
                       type="text"
                       @click="handleUpdate(scope.$index, scope.row)">
              收款
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


    <!--收款页面-->
    <el-dialog
      :title="'更新收款信息'"
      :visible.sync="skDialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="收款日期：" style="margin-bottom: 10px">
          <el-date-picker style="width: 350px"
                          v-model="mainEntry.receivable_date"
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
        <el-button @click="skDialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm()" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import {listPage, save, remove, update, updateStatus} from '@/api/sale/receivable'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  order_no: null,
  receive_money_type: null,
  duty_man: null,
  status: null,
}
const defaultEntry = {
  id: null,
  order_no: null,
  receive_money: null,
  receive_money_type: null,
  create_time: null,
  duty_man: null,
  remarks: null,
  status: null,
  type: null,
  receivable_date: null,
  financial_remarks: null,
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
  },
  {
    type: 4,
    desc: "保内维修应收款"
  },
  {
    type: 5,
    desc: "保外维修应收款"
  },
]
const statusEnum = [
  {
    type: 0,
    value: '未收'
  },
  {
    type: 1,
    value: '已收'
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
      skDialogVisible: false,
      isEdit: false,
      receiveMoneyTypeEnums,
      statusEnum,
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
    },
    statusFilter(status) {
      return status === 0 ? '未收' : '已收'
    },
    typeFilter(type) {
      return type === 1 ? '销售应收' : '售后应收'
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
      this.skDialogVisible = true;
      this.isEdit = true;
      this.mainEntry = Object.assign({}, row);
    },
    handleClose(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
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
            this.skDialogVisible = false;
            this.getList();
          })
        } else {
          this.mainEntry.status = 1
          if(this.receive_money_type===1 || this.receive_money_type===2) {
            this.mainEntry.type=1;
          }else {
            this.mainEntry.type=2;
          }
          save(this.mainEntry).then(response => {
            this.$message({
              message: '添加成功！',
              type: 'success'
            });
            this.dialogVisible = false;
            this.skDialogVisible = false;
            this.getList();
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
    handleReceivable(id, status) {
      this.$confirm('是否要更改应收单状态?', '提示', {
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
