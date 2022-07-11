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
            <el-input v-model="param.sc_order_no" class="input-width" placeholder="生产单单号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.production_name" class="input-width" placeholder="产品名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.production_type" class="input-width" placeholder="产品型号" clearable></el-input>
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
        <el-table-column label="生产单单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.sc_order_no }}</template>
        </el-table-column>
        <el-table-column label="类型" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.type | typeFilter }}</template>
        </el-table-column>
        <el-table-column label="产品名称" align="center">
          <template slot-scope="scope">{{ scope.row.production_name }}</template>
        </el-table-column>
        <el-table-column label="产品型号" align="center">
          <template slot-scope="scope">{{ scope.row.production_type }}</template>
        </el-table-column>
        <el-table-column label="生产数量" align="center">
          <template slot-scope="scope">{{ scope.row.total_num }}</template>
        </el-table-column>
        <el-table-column label="关联工序名称" align="center">
          <template slot-scope="scope">{{ scope.row.product_process_name }}</template>
        </el-table-column>
        <el-table-column label="工序步骤" align="center">
          <template slot-scope="scope">第{{ scope.row.process_step }}步</template>
        </el-table-column>
        <el-table-column label="质检状态" align="center">
          <template slot-scope="scope">{{ scope.row.quality_status | statusFilter }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini" v-if="!scope.row.quality_status ||scope.row.quality_status===2"
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
        <el-form-item label="生产单号：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.sc_order_no" disabled style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="生产单号：" style="margin-bottom: 10px">
          <el-input :value="mainEntry.type | typeFilter" disabled style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="产品名称：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.production_name" disabled style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="产品型号：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.production_type" disabled style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="生产数量：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.total_num" disabled style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="关联工艺：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.product_process_name" disabled style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="合格量：" style="margin-bottom: 10px">
          <el-input v-model="processEntry.access_num" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="质检状态：" style="margin-bottom: 10px">
          <el-radio v-model="processEntry.quality_status" :label="1" border>审核通过</el-radio>
          <el-radio v-model="processEntry.quality_status" :label="2" border>审核未通过</el-radio>
        </el-form-item>
        <el-form-item label="备注：" style="margin-bottom: 10px">
          <el-input v-model="processEntry.remarks"
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
import {listPage, remove} from '@/api/quality_restriction/productionProcessQuality'
import {updateProductionProcess} from '@/api/warehouse_storage/production_order'
const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  sc_order_no: null,
  production_name: null,
  production_type: null,
}
const defaultEntry = {
  id: null,
  sc_order_no: null,
  production_name: null,
  production_type: null,
  type:null,
  total_num: null,
  product_process_id: null,
  product_process_name: null,
  process_step: null,
  quality_status: null,
};

const defaultProcessEntry = {
  id: null,
  quality_num: null,
  access_num: null,
  quality_status: null,
  remarks: null,
  sc_order_no: null,
  product_process_quality_id: null,
};


export default {
  name: "ProductionProcessQuality",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      processEntry:Object.assign({}, defaultProcessEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
    }
  },
  created() {
    this.getList();
  },
  filters: {
    statusFilter(val) {
      if (!val) {
        return "N/A"
      }
      return val === 1 ? "审核通过" : "审核不通过"
    },
    typeFilter(type){
      return type===1?'成品':'零件';
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
        this.processEntry.id = this.mainEntry.product_process_id;
        this.processEntry.quality_num = this.mainEntry.total_num;
        this.processEntry.sc_order_no = this.mainEntry.sc_order_no;
        this.processEntry.product_process_quality_id = this.mainEntry.id;
        updateProductionProcess(this.processEntry).then(response => {
          this.$message({
            message: '修改成功！',
            type: 'success'
          });
          this.dialogVisible = false;
          this.getList();
          Object.assign({}, defaultProcessEntry)
        })
      })
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该工序检验信息?', '提示', {
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
