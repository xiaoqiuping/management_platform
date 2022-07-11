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
            <el-input v-model="param.purchasing_name" class="input-width" placeholder="采购名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.purchasing_unit" class="input-width" placeholder="采购单位" clearable></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <!--操作栏-->
    <div class="operate-container">
      <el-button size="primary" class="btn-add" @click="handleAdd()"><i class="el-icon-plus left_icon"></i>添加
      </el-button>
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
        <el-table-column label="入库单单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="采购名称" align="center">
          <template slot-scope="scope">{{ scope.row.purchasing_name }}</template>
        </el-table-column>
        <el-table-column label="采购单位" align="center">
          <template slot-scope="scope">{{ scope.row.purchasing_unit }}</template>
        </el-table-column>
        <el-table-column label="联系人" align="center">
          <template slot-scope="scope">{{ scope.row.contact }}</template>
        </el-table-column>
        <el-table-column label="联系人电话" align="center">
          <template slot-scope="scope">{{ scope.row.phone }}</template>
        </el-table-column>
        <el-table-column label="采购日期" align="center">
          <template slot-scope="scope">{{ scope.row.purchase_date }}</template>
        </el-table-column>
        <el-table-column label="采购价格" align="center">
          <template slot-scope="scope">{{ scope.row.purchase_price }}</template>
        </el-table-column>
        <el-table-column label="采购总价" align="center">
          <template slot-scope="scope">{{ scope.row.purchase_total_price }}</template>
        </el-table-column>
        <el-table-column label="采购单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.cg_order_no }}</template>
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
      :title="isEdit?'编辑采购入库单':'新增采购入库单'"
      :visible.sync="dialogVisible"
      @close="handleClose('warehouseOrderForm')"
      width="40%">
      <el-form :model="mainEntry"
               ref="warehouseOrderForm"
               :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="采购名称：" class="marginBottom22" prop="purchasing_name">
          <el-input v-model="mainEntry.purchasing_name" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="采购单位：" class="marginBottom22" prop="purchasing_unit">
          <el-input v-model="mainEntry.purchasing_unit" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="联系人：" class="marginBottom22" prop="contact">
          <el-input v-model="mainEntry.contact" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="联系人电话：" class="marginBottom22" prop="phone">
          <el-input v-model="mainEntry.phone" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="采购日期：" class="marginBottom22" prop="purchase_date">
          <el-date-picker
            v-model="mainEntry.purchase_date"
            type="date"
            align="right"
            value-format="yyyy-MM-dd" class="width450">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="采购价格：" class="marginBottom22" prop="purchase_price">
          <el-input v-model.number="mainEntry.purchase_price" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="采购总价：" class="marginBottom22" prop="purchase_total_price">
          <el-input v-model.number="mainEntry.purchase_total_price" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="采购单号：" class="marginBottom22" prop="cg_order_no">
          <el-input v-model="mainEntry.cg_order_no" class="width450"></el-input>
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
        <el-button @click="handleClose('warehouseOrderForm')" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm('warehouseOrderForm')" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listPage, save, remove, update} from '@/api/warehouse_storage/warehouse_order'
import {exportXmlWarehouseOrder} from '@/api/export/export'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  purchasing_name: null,
  purchasing_unit: null,
}
const defaultEntry = {
  id: null,
  purchasing_name: null,
  purchasing_unit: null,
  contact: null,
  phone: null,
  purchase_date: null,
  purchase_price: null,
  purchase_total_price: null,
  cg_order_no: null,
  remarks: null,
};
export default {
  name: "WarehouseOrder",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      rules: {
        purchasing_name: [
          {required: true, message: '采购名称不能为空'}
        ],
        purchasing_unit: [
          {required: true, message: '采购单位不能为空'}
        ],
        contact: [
          {required: true, message: '联系人不能为空'}
        ],
        phone: [
          {required: true, message: '联系人电话不能为空'}
        ],
        purchase_date: [
          {required: true, message: '采购日期不能为空'}
        ],
        purchase_price: [
          {required: true, message: '采购价格不能为空'},
          {type: 'number', message: '采购价格必须为数字值'}
        ],
        purchase_total_price: [
          {required: true, message: '采购总计不能为空'},
          {type: 'number', message: '采购总计必须为数字值'}
        ],
        cg_order_no: [
          {required: true, message: '采购单号不能为空'}
        ],
      }
    }
  },
  created() {
    this.getList();
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
    handleClose(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
      this.chooseMaterial = null;
      this.materialList = [];
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
      this.$confirm('是否要删除该入库单?', '提示', {
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
    handleExport() {
      this.$confirm('是否要导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        exportXmlWarehouseOrder(this.param).then(res => {
          this.$message({
            message: '导出成功！',
            type: 'success'
          });
          const link = document.createElement('a')
          let blob = new Blob([res.data], {type: 'application/vnd.ms-excel'});
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

