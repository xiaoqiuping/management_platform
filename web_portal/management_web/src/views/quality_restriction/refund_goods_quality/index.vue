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
            <el-input v-model="param.watch_name" class="input-width" placeholder="手表名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.watch_type" class="input-width" placeholder="手表型号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.status" placeholder="请选择状态" clearable @clear="param.status = null">
              <el-option
                v-for="status in statusEnum"
                :key="status.value"
                :label="status.value"
                :value="status.type">
              </el-option>
            </el-select>
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
        <el-table-column label="维修单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="手表名称" align="center">
          <template slot-scope="scope">{{ scope.row.watch_name }}</template>
        </el-table-column>
        <el-table-column label="手表型号" align="center">
          <template slot-scope="scope">{{ scope.row.watch_type }}</template>
        </el-table-column>
        <el-table-column label="客户名称" align="center">
          <template slot-scope="scope">{{ scope.row.buyer_name }}</template>
        </el-table-column>
        <el-table-column label="维修金额" align="center">
          <template slot-scope="scope">{{ scope.row.repair_amount }}</template>
        </el-table-column>
        <el-table-column label="维修人" align="center">
          <template slot-scope="scope">{{ scope.row.repair_man }}</template>
        </el-table-column>
        <el-table-column label="维修日期" align="center">
          <template slot-scope="scope">{{ scope.row.repair_date }}</template>
        </el-table-column>
        <el-table-column label="送回日期" align="center">
          <template slot-scope="scope">{{ scope.row.send_back_date }}</template>
        </el-table-column>
        <el-table-column label="订单状态" align="center">
          <template slot-scope="scope">{{ scope.row.status |statusFilter }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleDrawer(scope.$index, scope.row)">
              详情
            </el-button>
            <el-button size="mini" v-if="scope.row.status===1 || scope.row.status===2"
                       type="text"
                       @click="handleUpdate(scope.$index, scope.row)">
              审核
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
      :title="isEdit?'编辑维修单':'新增维修单'"
      :visible.sync="dialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               ref="adminForm"
               label-width="150px" size="small">

        <el-form-item label="手表名称：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.watch_name" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="手表型号：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.watch_type" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="购买日期：" style="margin-bottom: 10px">
          <el-date-picker style="width: 350px" disabled
                          v-model="mainEntry.buy_date"
                          align="right"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="保修截止日期：" style="margin-bottom: 10px">
          <el-date-picker style="width: 350px" disabled
                          v-model="mainEntry.guarantee_date"
                          align="right"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="客户名称：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.buyer_name" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="客户联系电话：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.buyer_phone" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="客户地址：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.buyer_address" disabled
                    type="textarea"
                    maxlength="100"
                    :rows="2"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="故障描述：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.fault_description" disabled
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="维修金额：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.repair_amount" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="维修人：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.repair_man" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="维修日期：" style="margin-bottom: 10px">
          <el-date-picker style="width: 350px" disabled
                          v-model="mainEntry.repair_date"
                          align="right"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="维修分析：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.repair_analysis" disabled
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="送回日期：" style="margin-bottom: 10px">
          <el-date-picker style="width: 350px" disabled
                          v-model="mainEntry.send_back_date"
                          align="right"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.remarks" disabled
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="审核状态：" style="margin-bottom: 10px">
          <el-radio v-model="mainEntry.status" :label="3" border>审核通过</el-radio>
          <el-radio v-model="mainEntry.status" :label="2" border>审核未通过</el-radio>
        </el-form-item>
        <el-form-item label="备注：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.examine_remarks"
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
    <!--详情-->
    <el-drawer
      title="我是标题"
      size="45%"
      :visible.sync="drawerVisible"
      :with-header="false">
      <el-card class="box-card" style="height: 100%">
        <div slot="header" class="clearfix">
          <span>维修单号：{{ mainEntry.order_no }}</span>
        </div>
        <div>
          <el-form :model="mainEntry"
                   label-width="150px" size="small">
            <el-form-item label="维修类别：" style="margin-bottom: 10px">
              <span class="margintop10">保内维修</span>
            </el-form-item>
            <el-form-item label="手表名称：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.watch_name }}</span>
            </el-form-item>
            <el-form-item label="手表型号：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.watch_type }}</span>
            </el-form-item>
            <el-form-item label="购买日期：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.buy_date }}</span>
            </el-form-item>
            <el-form-item label="保修截止日期：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.guarantee_date }}</span>
            </el-form-item>
            <el-form-item label="客户名称：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.buyer_name }}</span>
            </el-form-item>
            <el-form-item label="客户联系电话：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.buyer_phone }}</span>
            </el-form-item>
            <el-form-item label="故障描述：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.fault_description }}</span>
            </el-form-item>
            <el-form-item label="维修金额：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.repair_amount }}</span>
            </el-form-item>
            <el-form-item label="维修人：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.repair_man }}</span>
            </el-form-item>
            <el-form-item label="维修日期：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.repair_date }}</span>
            </el-form-item>
            <el-form-item label="维修分析：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.repair_analysis }}</span>
            </el-form-item>
            <el-form-item label="送回日期：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.send_back_date }}</span>
            </el-form-item>
            <el-form-item label="备注：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.remarks }}</span>
            </el-form-item>
            <el-form-item label="订单状态：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.status  | statusFilter }}</span>
            </el-form-item>
            <el-form-item label="质检人：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.examine_man }}</span>
            </el-form-item>
            <el-form-item label="质检备注：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.examine_remarks }}</span>
            </el-form-item>

          </el-form>
        </div>
      </el-card>
    </el-drawer>
  </div>
</template>


<script>
import {listPage, examineOrder} from '@/api/after_sale_repair/repairOrder'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  type: 1,
  from: 2,
  status: null,
  watch_name: null,
  watch_type: null,
}
const defaultEntry = {
  id: null,
  order_no: null,
  watch_name: null,
  watch_type: null,
  buy_date: null,
  guarantee_date: null,
  buyer_name: null,
  buyer_phone: null,
  buyer_address: null,
  fault_description: null,
  repair_amount: null,
  repair_man: null,
  repair_date: null,
  repair_analysis: null,
  send_back_date: null,
  remarks: null,
  type: 1,
  status: null,
  examine_man: null,
  examine_remarks: null,
};

const statusEnum = [
  {
    type: 1,
    value: "新建",
  },
  {
    type: 2,
    value: "质检未通过",
  },
  {
    type: 3,
    value: "质检通过",
  },
]
/*保内维修审核*/
export default {
  name: "WarrantyPeriod",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      drawerVisible: false,
      statusEnum,
    }
  },
  created() {
    this.getList();
  },
  filters: {
    statusFilter(status) {
      if (!status) {
        return "N/A"
      }
      let find = statusEnum.find(s => s.type === status)
      if (find) {
        return find.value;
      }
      return "N/A"
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
        examineOrder(this.mainEntry).then(response => {
          this.$message({
            message: '审核成功！',
            type: 'success'
          });
          this.dialogVisible = false;
          this.getList();
        })

      })
    },
    //唤起抽屉
    handleDrawer(index, row) {
      this.drawerVisible = true;
      this.mainEntry = Object.assign({}, row);
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>
