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
            <el-input v-model="param.watch_name" class="input-width" placeholder="手表名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.watch_type" class="input-width" placeholder="手表型号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.buyer" class="input-width" placeholder="购买商" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.salesman" class="input-width" placeholder="销售人" clearable></el-input>
          </el-form-item>
          <el-form-item style="margin-bottom: 10px">
            <el-date-picker
              v-model="sale_date"
              type="daterange"
              align="right"
              value-format="yyyy-MM-dd"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <!--操作栏-->
    <div class="operate-container">
      <el-button class="btn-add" @click="handleReset()"><i class="el-icon-refresh left_icon"></i>刷新</el-button>
    </div>
    <!--表格-->
    <div class="table-container">
      <el-table ref="adminTable"
                :data="list"
                style="width: 100%;"
                v-loading="listLoading" >
        <el-table-column label="单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="手表名称" align="center">
          <template slot-scope="scope">{{ scope.row.watch_name}}</template>
        </el-table-column>
        <el-table-column label="手表型号" align="center">
          <template slot-scope="scope">{{ scope.row.watch_type }}</template>
        </el-table-column>
        <el-table-column label="销售数量" align="center">
          <template slot-scope="scope">{{ scope.row.sale_num }}</template>
        </el-table-column>
        <el-table-column label="成交价格" align="center">
          <template slot-scope="scope">{{ scope.row.tran_price }}</template>
        </el-table-column>
        <el-table-column label="购买商" align="center">
          <template slot-scope="scope">{{ scope.row.buyer }}</template>
        </el-table-column>
        <el-table-column label="销售人" align="center">
          <template slot-scope="scope">{{ scope.row.salesman }}</template>
        </el-table-column>
        <el-table-column label="销售时间" align="center">
          <template slot-scope="scope">{{ scope.row.sale_date }}</template>
        </el-table-column>
        <el-table-column label="销售状态" align="center">
          <template slot-scope="scope">{{ scope.row.status|orderStatusFilter  }}</template>
<!--          <template slot-scope="scope">-->
<!--            <el-tag type="success" v-if="scope.row.status===5">{{ scope.row.status |orderStatusFilter }}</el-tag>-->
<!--            <el-tag type="danger" v-if="scope.row.status===2">{{ scope.row.status |orderStatusFilter }}</el-tag>-->
<!--            <el-tag v-if="scope.row.status!==2 && scope.row.status!==5">{{-->
<!--                scope.row.status |orderStatusFilter-->
<!--              }}-->
<!--            </el-tag>-->
<!--          </template>-->
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope" v-if="scope.row.status!==5">
            <el-button size="mini" type="text" @click="handleUpdate(scope.$index, scope.row)" v-if="scope.row.status===1">编辑</el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.$index, scope.row)" v-if="scope.row.status===1">删除</el-button>
            <el-button size="mini" type="text" @click="handleGK(scope.$index, scope.row)" v-if="scope.row.status===1">改款</el-button>
            <el-button size="mini" type="text">
              <span v-if="scope.row.status <3 && scope.row.status!==2" @click="handleUpdateStatus(scope.$index, scope.row,2)">挂起</span>
              <span v-if="scope.row.status <3 &&scope.row.status===2" @click="handleUpdateStatus(scope.$index, scope.row,1)">恢复</span>
            </el-button>
            <el-button size="mini" type="text" v-if="scope.row.status===3"
                       @click="handleUpdateStatus(scope.$index, scope.row,5)">成交
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--页脚-->
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
      :title="isEdit?'编辑销售订单':'新增销售订单'"
      :visible.sync="dialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="手表名称：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.watch_name" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="手表型号：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.watch_type" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="销售数量：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.sale_num" style="width: 350px" v-show="!isEdit"></el-input>
          <el-input v-model="mainEntry.sale_num" style="width: 350px" v-show="isEdit" disabled></el-input>
        </el-form-item>
        <el-form-item label="成交价格：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.tran_price" style="width: 350px" v-show="!isEdit"></el-input>
          <el-input v-model="mainEntry.tran_price" style="width: 350px" v-show="isEdit" disabled></el-input>
        </el-form-item>
        <el-form-item label="购买商：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.buyer" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="销售人：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.salesman" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="销售日期：" style="margin-bottom: 10px">
          <el-date-picker
            v-model="mainEntry.sale_date"
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
    <!--订单改款页面-->
    <el-dialog
      :title="'订单改款'"
      :visible.sync="gkDialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="手表型号：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.watch_type" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="销售数量：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.sale_num" style="width: 350px" ></el-input>
        </el-form-item>
        <el-form-item label="成交价格：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.tran_price" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="购买商：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.buyer" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="销售人：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.salesman" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="销售日期：" style="margin-bottom: 10px">
          <el-date-picker
            disabled
            v-model="mainEntry.sale_date"
            align="right"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="gkDialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleUpdateChangeFlag()" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listPage, remove, update, updateStatus} from '@/api/sale/order'
import {orderStatusEnums} from '@/const/saleOrderStatus'
const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  order_no: null,
  watch_name:null,
  watch_type: null,
  buyer: null,
  salesman: null,
  sale_start_date: null,
  sale_end_date: null,
  status_list:[1,2,5],
  change_flag:true,
}
const defaultEntry = {
  id: null,
  order_no: null,
  watch_name:null,
  watch_type: null,
  sale_num: null,
  tran_price: null,
  buyer: null,
  salesman: null,
  sale_date: null,
  status: null,
  change_flag: null,
}
export default {
  name: "ChangeOrderList",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      sale_date: null,
      gkDialogVisible:false,
      orderStatusEnums,

    }
  },
  created() {
    this.getList();
  },
  filters: {
    orderStatusFilter(type) {
      if (!type) {
        return "N/A"
      }
      return orderStatusEnums.find(r => r.type === type).value;
    }
  },
  methods: {
    handleResetSearch() {
      this.page = Object.assign({}, defaultPage);
      this.param = Object.assign({}, defaultParam);
      this.sale_date = null;
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
      if (this.sale_date) {
        this.param.sale_start_date = this.sale_date[0]
        this.param.sale_end_date = this.sale_date[1]
      } else {
        this.param.sale_start_date = null;
        this.param.sale_end_date = null;
      }
      listPage(this.param, this.page).then(response => {
        this.listLoading = false;
        this.list = response.data.content;
        this.page.total = response.data.totalElements;
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
    //改款（唤起dialog）
    handleGK(index, row) {
      this.gkDialogVisible = true;
      this.isEdit = true;
      this.mainEntry = Object.assign({}, row);
    },
    //保存或更新信息
    handleDialogConfirm() {
      this.$confirm('是否确认更新?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        save(this.mainEntry).then(response => {
          this.$message({
            message: '添加成功！',
            type: 'success'
          });
          this.dialogVisible = false;
          this.getList();
        })
      })
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该销售单?', '提示', {
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
    //更改订单状态
    handleUpdateStatus(index, row, status) {
      this.$confirm('是否要更改订单状态?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.status = status
        updateStatus(row).then(response => {
          this.$message({
            type: 'success',
            message: '更改状态成功!'
          });
          this.getList();
        });
      });
    },
    handleUpdateChangeFlag(index, row, status) {
      this.$confirm('是否要更改订单数量或成交价格?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.mainEntry.change_flag = true;
        update(this.mainEntry).then(response => {
          this.$message({
            message: '修改成功！',
            type: 'success'
          });
          this.gkDialogVisible = false;
          this.getList();
        })
      });
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>
