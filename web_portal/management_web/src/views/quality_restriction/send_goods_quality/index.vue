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
            <el-input v-model="param.express_no" class="input-width" placeholder="快递单号" clearable></el-input>
          </el-form-item>
          <!--          <el-form-item>-->
          <!--            <el-input v-model="param.production_name" class="input-width" placeholder="产品名称" clearable></el-input>-->
          <!--          </el-form-item>-->
        </el-form>
      </div>
    </el-card>
    <!--操作栏-->
    <div class="operate-container">
      <!--      <el-button size="primary" class="btn-add" @click="handleAdd()"><i class="el-icon-plus left_icon"></i>添加-->
      <!--      </el-button>-->
      <el-button class="btn-add" @click="handleReset()"><i class="el-icon-refresh left_icon"></i>刷新
      </el-button>
    </div>
    <!--表格-->
    <div class="table-container">
      <el-table ref="adminTable"
                :data="list"
                style="width: 100%;"
                v-loading="listLoading">
        <el-table-column label="发货单单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="产品名称" align="center">
          <template slot-scope="scope">{{ scope.row.production_name }}</template>
        </el-table-column>
        <el-table-column label="产品型号" align="center">
          <template slot-scope="scope">{{ scope.row.production_type }}</template>
        </el-table-column>
        <el-table-column label="数量" align="center">
          <template slot-scope="scope">{{ scope.row.total_num }}</template>
        </el-table-column>
        <el-table-column label="客户名称" align="center">
          <template slot-scope="scope">{{ scope.row.buyer }}</template>
        </el-table-column>
        <el-table-column label="客户电话" align="center">
          <template slot-scope="scope">{{ scope.row.buyer_phone }}</template>
        </el-table-column>
        <el-table-column label="客户地址" align="center">
          <template slot-scope="scope">{{ scope.row.buyer_address }}</template>
        </el-table-column>
        <el-table-column label="快递方式" align="center">
          <template slot-scope="scope">{{ scope.row.express_type }}</template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">{{ scope.row.status | statusFilter }}</template>
        </el-table-column>
        <el-table-column label="销售单单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.sale_order_no }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{ scope.row.remarks }}</template>
        </el-table-column>
        <el-table-column label="发货时间" align="center" width="100px">
          <template slot-scope="scope">{{ scope.row.send_date }}</template>
        </el-table-column>
        <el-table-column label="发货单号" align="center">
          <template slot-scope="scope">{{ scope.row.express_no }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini" v-if="scope.row.status===2 || scope.row.status===3"
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
        :total="total">
      </el-pagination>
    </div>
    <!--添加编辑页面-->
    <el-dialog
      :title="'审核发货单'"
      :visible.sync="dialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="产品名称/型号" style="margin-bottom: 10px">
          <el-input :value="`${(mainEntry.production_name)}/${(mainEntry.production_type)}`" disabled
                    style="width: 350px" v-if="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="数量" style="margin-bottom: 10px" width="220px">
          <el-input v-model="mainEntry.total_num" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="销售单单号" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.sale_order_no" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="客户名称" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.buyer" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="客户电话" style="margin-bottom: 10px" width="220px">
          <el-input v-model="mainEntry.buyer_phone" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="客户地址" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.buyer_address" disabled
                    type="textarea"
                    maxlength="100"
                    :rows="2"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="快递方式" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.express_type" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="备注" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.remarks" disabled
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="审核状态：" style="margin-bottom: 10px">
          <el-radio v-model="mainEntry.status" :label="4" border>审核通过</el-radio>
          <el-radio v-model="mainEntry.status" :label="3" border @change="cleanVal(mainEntry)">审核未通过</el-radio>
        </el-form-item>
        <el-form-item label="快递单号" style="margin-bottom: 10px" v-if="mainEntry.status===4">
          <el-input v-model="mainEntry.express_no" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="发货日期：" style="margin-bottom: 10px" v-if="mainEntry.status===4">
          <el-date-picker style="width: 350px"
                          v-model="mainEntry.send_date"
                          align="right"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
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
import {listPage, update} from '@/api/sale/sendGoodsOrder'
import {sendGoodsOrderStatus} from "@/const/sendGoodsOrder"

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  express_no: null,
  production_name: null,
}
const defaultEntry = {
  id: null,
  order_no: null,
  production_name: null,
  total_num: null,
  sale_order_no: null,
  buyer: null,
  buyer_phone: null,
  buyer_address: null,
  express_type: null,
  sender: null,
  send_date: null,
  express_no: null,
  status: null,
  remarks: null,

};
export default {
  name: "SendGoodsOrder",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      total: null,
      sendGoodsOrderStatus,
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
      return sendGoodsOrderStatus.find(r => r.type === status).value;
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
        this.total = response.data.totalElements;
      });
    },
    handleReset() {
      this.getList();
    },
    //更新（唤起dialog）
    handleUpdate(index, row) {
      this.dialogVisible = true;
      this.isEdit = true;
      this.mainEntry = Object.assign({}, row);
    },
    cleanVal(mainEntry) {
      mainEntry.express_no = null;
      mainEntry.send_date = null;
    },
    //保存或更新信息
    handleDialogConfirm() {
      this.$confirm('是否确认保存?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        update(this.mainEntry).then(response => {
          this.$message({
            message: '修改成功！',
            type: 'success'
          });
          this.dialogVisible = false;
          this.getList();
        })
      })
    },
  }
}
</script>
<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>
