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
        <el-table-column label="生产订单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
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
        <el-table-column label="生产总数量" align="center">
          <template slot-scope="scope">{{ scope.row.total_num }}</template>
        </el-table-column>
        <el-table-column label="单价" align="center">
          <template slot-scope="scope">{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column label="总金额" align="center">
          <template slot-scope="scope">{{ scope.row.total_price }}</template>
        </el-table-column>
        <el-table-column label="订单周期" align="center">
          <template slot-scope="scope">{{ scope.row.order_cycle }}</template>
        </el-table-column>
        <el-table-column label="订单状态" align="center">
          <template slot-scope="scope">{{ scope.row.status | orderStatusFilter }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleDrawer(scope.$index, scope.row)">
              详情
            </el-button>
            <el-button size="mini" v-if="scope.row.status<7"
                       type="text"
                       @click="handleExamine(scope.$index, scope.row)">
              送检
            </el-button>
            <el-button size="mini"
                       type="text"
                       @click="handleFinalExamine( scope.row.id,8)" v-if="scope.row.status===7">
              成品送检
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
    <!--详情-->
    <el-drawer
      title="我是标题"
      size="45%"
      :visible.sync="drawerVisible"
      :with-header="false">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>单号：{{ mainEntry.order_no }}</span>
        </div>
        <div style="height: 600px;overflow-y: auto">
          <el-form :model="mainEntry"
                   label-width="150px" size="small">
            <el-form-item label="类型：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.type | typeFilter }}</span>
            </el-form-item>
            <el-form-item label="产品名称：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.production_name }}</span>
            </el-form-item>
            <el-form-item label="产品型号：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.production_type }}</span>
            </el-form-item>
            <el-form-item label="生产总数量：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.total_num }}</span>
            </el-form-item>
            <el-form-item label="单价：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.price }}</span>
            </el-form-item>
            <el-form-item label="总金额：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.total_price }}</span>
            </el-form-item>
            <el-form-item label="订单周期（天）：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.order_cycle }}</span>
            </el-form-item>
            <el-form-item label="订单创建时间：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.order_create_time }}</span>
            </el-form-item>
            <el-form-item label="完成日期：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.production_finished_date }}</span>
            </el-form-item>
            <el-form-item label="发货形式：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.send_goods_type }}</span>
            </el-form-item>
            <el-form-item label="订单要求：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.order_requirement }}</span>
            </el-form-item>
            <el-form-item label="销售单号：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.sale_order_no }}</span>
            </el-form-item>
            <el-form-item label="备注：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.remarks }}</span>
            </el-form-item>
            <el-form-item label="审核人：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.verify_man }}</span>
            </el-form-item>
            <el-form-item label="审核状态：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.verify_status  | orderStatusFilter }}</span>
            </el-form-item>
            <el-form-item label="审核意见：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.verify_remarks }}</span>
            </el-form-item>
          </el-form>
        </div>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>订单状态：{{ mainEntry.status | orderStatusFilter }}</span>
        </div>
        <el-steps :active="stepActive" align-center style="margin: 20px 0">
          <el-step
            :title="pro.process_name"
            :status="pro.quality_status|qualityStatusFilter"
            :description="`'质检数量'-${(pro.access_num?pro.access_num:0)}/${(pro.quality_num?pro.quality_num:0)}`"
            v-for="pro in mainEntry.process_list" :key="pro.process_step">{{ pro.quality_status|qualityStatusFilter }}
          </el-step>
        </el-steps>
      </el-card>
    </el-drawer>
    <!--送检-->
    <el-drawer
      title="我是标题"
      size="45%"
      :visible.sync="sjDrawerVisible"
      :with-header="false">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>单号：{{ mainEntry.order_no }}</span>
        </div>
      </el-card>
      <el-card class="box-card" style="height: 100%;overflow-y: auto;padding: 0">
        <div slot="header" class="clearfix">
          <span>订单状态：{{ mainEntry.status | orderStatusFilter }}</span>
        </div>
        <div style="padding: 30px 0 30px 100px">
          <el-steps direction="vertical" space="100px">
            <el-step v-for="(pro,index) in mainEntry.process_list"
                     :status="pro.quality_status|qualityStatusFilter"
                     :title="pro.process_name" :key="pro.id+''">
              {{ pro.quality_status|qualityStatusFilter }}
              <template slot="description">
                <span>质检数量-{{ pro.access_num ? pro.access_num : 0 }}/{{ pro.quality_num ? pro.quality_num : 0 }}</span>
                <el-button size="mini" round style="margin-left: 10px"
                           v-if="showSJ(index,pro,mainEntry.process_list)" @click="examineProcess(mainEntry,pro)">送检
                </el-button>
                <br>
                <span v-if="pro.quality_man">质检人：{{ pro.quality_man }}</span><br>
                <span v-if="pro.quality_time">质检时间：{{ pro.quality_time }}</span><br>
                <span v-if="pro.remarks">质检意见：{{ pro.remarks }}</span>
              </template>
            </el-step>
          </el-steps>
        </div>
      </el-card>
    </el-drawer>
  </div>
</template>

<script>
import {listPage, examineProcess,updateStatus} from '@/api/warehouse_storage/production_order'
import {scOrderStatus} from "@/const/SCOrderStatus"
import {exportXmlProductionOrder} from '@/api/export/export'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  production_name: null,
  production_type: null,
  from:'SCGL',
}
const defaultEntry = {
  id: null,
  order_no: null,
  production_name: null,
  production_type: null,
  type: null,
  total_num: null,
  price: null,
  total_price: null,
  order_cycle: null,
  production_finished_date: null,
  send_goods_type: null,
  order_requirement: null,
  status: null,
  order_create_time: null,
  sale_order_no: null,
  remarks: null,
  verify_status: null,
  verify_man: null,
  verify_remarks: null,
  process_list: [],
};

export default {
  name: "ProductionOrder",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      drawerVisible: false,
      sjDrawerVisible: false,
      stepActive: 0,
      scOrderStatus,
    }
  },
  created() {
    this.getList();
  },
  filters: {
    orderStatusFilter(val) {
      if (!val) {
        return "N/A"
      }
      return scOrderStatus.find(o => o.value === val).label
    },
    qualityStatusFilter(status) {
      if (status == null) {
        return 'process';
      }
      return status === 1 ? 'success' : 'error';
    },
    typeFilter(type) {
      return type === 1 ? '成品' : '零件';
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
    //是否显示送检按钮
    showSJ(index, pro, processList) {
      //如果成功，不需要
      if (pro.quality_status === 1) {
        return false;
      } else {
        //上一个是否有送检按钮
        let above = processList.find(p => p.process_step === index)
        if (above) {
          if (above.quality_status !== 1) {
            return false;
          } else {
            return true;
          }
        } else {
          //第一个
          return pro.quality_status === 1 || pro.quality_status == null
        }
      }
    },
    //唤起抽屉
    handleDrawer(index, row) {
      this.drawerVisible = true;
      this.mainEntry = Object.assign({}, row);
    },
    //更新（唤起dialog）
    handleExamine(index, row) {
      this.sjDrawerVisible = true;
      this.mainEntry = Object.assign({}, row);
    },
    examineProcess(entry, pro) {
      this.$confirm('是否确认送检?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        entry.process_list = [];
        entry.process_list.push(pro)
        examineProcess(this.mainEntry).then(response => {
          this.$message({
            message: '送检成功！',
            type: 'success'
          });
        })
        this.sjDrawerVisible = false;
      })
    },
    handleFinalExamine(id, status) {
      this.$confirm('是否确认成品送检?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateStatus(id, status).then(response => {
          this.$message({
            message: '送检成功！',
            type: 'success'
          });
          this.getList();
        })
      })
    },
    handleExport() {
      this.$confirm('是否要导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        exportXmlProductionOrder(this.param).then(res => {
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
  },

}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>

