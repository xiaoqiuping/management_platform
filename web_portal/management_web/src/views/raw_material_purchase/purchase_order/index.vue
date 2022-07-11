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
            <el-input v-model="param.purchase_man" class="input-width" placeholder="采购人" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.status" clearable placeholder="请选择订单状态" @clear="param.status=null">
              <el-option
                v-for="status in purchaseOrderStatus"
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
      <!--      <el-button size="primary" class="btn-add" @click="handleAdd(0)"><i class="el-icon-plus left_icon"></i>新增-->
      <!--      </el-button>-->
      <el-button size="primary" class="btn-add" @click="handleExport()"><i class="el-icon-paperclip left_icon"></i>导出
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
        <el-table-column label="采购单单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="创建人" align="center">
          <template slot-scope="scope">{{ scope.row.duty_man }}</template>
        </el-table-column>
        <el-table-column label="采购人" align="center">
          <template slot-scope="scope">{{ scope.row.purchase_man }}</template>
        </el-table-column>
        <el-table-column label="采购时间" align="center">
          <template slot-scope="scope">{{ scope.row.purchase_date }}</template>
        </el-table-column>
        <el-table-column label="订单状态" align="center">
          <template slot-scope="scope">{{ scope.row.status | statusTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleDrawer(scope.$index, scope.row)">详情</el-button>
            <el-button size="mini" v-if="scope.row.status<2"
                       type="text"
                       @click="handleUpdate(scope.row.id,2)">采购</el-button>
            <el-button size="mini" v-if="scope.row.status<3"
                       type="text"
                       @click="handleUpdate(scope.row.id,3)">采购完成</el-button>
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
        :total="total_num">
      </el-pagination>
    </div>
    <!--详情-->
    <el-drawer
      title="我是标题"
      size="45%"
      :visible.sync="drawerVisible"
      :with-header="false">
      <el-card class="box-card" style="height: 100%">
        <div slot="header" class="clearfix">
          <span>单号：{{ mainEntry.order_no }}</span>
        </div>
        <div style="height: 100%;overflow-y: auto">
          <el-form :model="mainEntry"
                   label-width="150px" size="small">
            <el-form-item label="创建人：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.duty_man }}</span>
            </el-form-item>
            <el-form-item label="采购人：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.purchase_man }}</span>
            </el-form-item>
            <el-form-item label="采购时间：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.purchase_date }}</span>
            </el-form-item>
            <el-form-item label="订单状态：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.status | statusTypeFilter }}</span>
            </el-form-item>
            <el-form-item label="备注：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.remarks }}</span>
            </el-form-item>
            <el-form-item label="原料详情：" style="margin-bottom: 10px">
              <div class="table-container" style="margin-top: 0">
                <el-table ref="adminTable"
                          :data="mainEntry.raw_material_list"
                          style="width: 100%;"
                          v-loading="listLoading" >
                  <el-table-column label="原料名称" align="center">
                    <template slot-scope="scope">{{ scope.row.raw_material_name }}</template>
                  </el-table-column>
                  <el-table-column label="原料价格" align="center">
                    <template slot-scope="scope">{{ scope.row.price }}</template>
                  </el-table-column>
                  <el-table-column label="原料数量" align="center">
                    <template slot-scope="scope">{{ scope.row.total_num }}</template>
                  </el-table-column>
                </el-table>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </el-drawer>
  </div>
</template>

<script>
import {listPage, updateStatus} from '@/api/raw_materials_purchase/purchaseOrder'
import {purchaseOrderStatus} from '@/const/purchaseOrder'
import {exportXmlPurchaseOrder} from '@/api/export/export'

const defaultPage = {
  page: 1,
  size: 10,
}
const defaultParam = {
  purchase_man: null,
  status: null,
}
const defaultEntry = {
  id: null,
  order_no: null,
  duty_man: null,
  purchase_man: null,
  purchase_date: null,
  status: null,
  remarks: null,
  raw_material_list: [
    {
      raw_material_name: null,
      price: null,
      total_num: null,
    }
  ]
};
export default {
  name: "RawMaterial",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      materialList: null,
      total_num: 0,
      drawerVisible:false,
      purchaseOrderStatus,
    }
  },
  created() {
    this.getList();
  },
  filters: {
    statusTypeFilter(val) {
      if (val) {
        return purchaseOrderStatus.filter(s => s.type === val)[0].value;
      }
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
        this.total_num = response.data.totalElements;
      });
    },
    //唤起抽屉
    handleDrawer(index, row) {
      console.log(row)
      this.drawerVisible = true;
      this.mainEntry = Object.assign({}, row);
    },
    handleReset() {
      this.getList();
    },
    handleUpdate(id,type) {
      this.$confirm('是否要更改订单状态吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateStatus(id, type).then(response => {
          this.$message({
            type: 'success',
            message: '更改成功!'
          });
          this.getList();
        });
      });
    },
    handleExport() {
      this.$confirm('是否要导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        exportXmlPurchaseOrder(this.param).then(res => {
          this.$message({
            message: '导出成功！',
            type: 'success'
          });
          const link = document.createElement('a')
          let blob = new Blob([res.data],{type: 'application/vnd.ms-excel'});
          //获取heads中的filename文件名
          let temp = res.headers["content-disposition"].split(";")[1].split("filename=")[1];
          //对文件名乱码转义--【Node.js】使用iconv-lite解决中文乱码
          let iconv = require('iconv-lite');
          iconv.skipDecodeWarning = true;//忽略警告
          let fileName = iconv.decode(temp, 'gbk');
          link.style.display = 'none'
          link.href = URL.createObjectURL(blob);
          link.setAttribute('download', fileName)
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
        })
      });
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>
