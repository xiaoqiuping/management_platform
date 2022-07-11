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
            <el-input v-model="param.duty_man" class="input-width" placeholder="负责人" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.status" clearable placeholder="请选择订单状态" @clear="param.status=null">
              <el-option
                v-for="status in statusEnums"
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
      <el-button size="primary" class="btn-add" @click="handleAdd()"><i class="el-icon-plus left_icon"></i>新增
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

        <el-table-column label="委派单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="产品名称" align="center">
          <template slot-scope="scope">{{ scope.row.production_name }}</template>
        </el-table-column>
        <el-table-column label="产品数量" align="center">
          <template slot-scope="scope">{{ scope.row.production_num }}</template>
        </el-table-column>
        <el-table-column label="支付费用" align="center">
          <template slot-scope="scope">{{ scope.row.cost }}</template>
        </el-table-column>
        <el-table-column label="完工日期" align="center">
          <template slot-scope="scope">{{ scope.row.finished_date }}</template>
        </el-table-column>
        <el-table-column label="订单状态" align="center">
          <template slot-scope="scope">{{ scope.row.status | statusFilter }}</template>
        </el-table-column>
        <el-table-column label="委外厂家" align="center">
          <template slot-scope="scope">{{ scope.row.outsource_unit }}</template>
        </el-table-column>
        <el-table-column label="委外厂家联系人" align="center">
          <template slot-scope="scope">{{ scope.row.contact }}</template>
        </el-table-column>
        <el-table-column label="联系人电话" align="center">
          <template slot-scope="scope">{{ scope.row.phone }}</template>
        </el-table-column>
        <el-table-column label="负责人" align="center">
          <template slot-scope="scope">{{ scope.row.duty_man }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{ scope.row.remarks }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status<2" size="mini" type="text" @click="updateStatus(scope.row,2)">外派</el-button>
            <el-button v-if="scope.row.status===2" size="mini" type="text" @click="updateStatus(scope.row,3)">完成</el-button>

            <el-button v-if="scope.row.status===1" size="mini" type="text"
                       @click="handleUpdate(scope.$index, scope.row)">编辑
            </el-button>
            <el-button v-if="scope.row.status===1" size="mini" type="text"
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
        :total="total_num">
      </el-pagination>
    </div>
    <!--添加编辑页面-->
    <el-dialog
      :title="isEdit?'编辑委外单信息':'新增委外单信息'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="产品名称：" class="marginBottom22" prop="production_name">
          <el-input v-model="mainEntry.production_name" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="产品数量：" class="marginBottom22" prop="production_num">
          <el-input v-model.number="mainEntry.production_num" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="支付费用：" class="marginBottom22">
          <el-input v-model="mainEntry.cost" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="完工日期：" class="marginBottom22">
          <el-date-picker
            v-model="mainEntry.finished_date"
            type="date"
            align="right"
            value-format="yyyy-MM-dd" class="width450">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="委外厂家：" class="marginBottom22">
          <el-input v-model="mainEntry.outsource_unit"
                    type="textarea"
                    maxlength="100"
                    :rows="2"
                    show-word-limit
                    class="width450"></el-input>
        </el-form-item>
        <el-form-item label="委外厂家联系人：" class="marginBottom22" prop="contact">
          <el-input v-model="mainEntry.contact" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="联系人电话：" class="marginBottom22" prop="phone">
          <el-input v-model="mainEntry.phone" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="负责人：" class="marginBottom22">
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
import {listPage, save, remove, update, updateStatus} from '@/api/production_plan/outsourceOrder'
import {exportXmlOutSourceOrder} from '@/api/export/export'

const defaultPage = {
  page: 1,
  size: 10,
}
const defaultParam = {
  status: null,
  duty_man: null,
  production_name: null,
}
const defaultEntry = {
  id: null,
  order_no: null,
  production_name: null,
  production_num: null,
  cost: null,
  finished_date: null,
  outsource_unit: null,
  contact: null,
  phone: null,
  duty_man: null,
  status: null,
  remarks: null,
}
const statusEnums = [
  {
    type: 1,
    value: "新建"
  },
  {
    type: 2,
    value: "委外中"
  },
  {
    type: 3,
    value: "完成"
  },
]

export default {
  name: "OutsourceOrder",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      total_num: 0,
      statusEnums,
      rules: {
        production_name: [
          {required: true, message: '产品名称不能为空'},
        ],
        production_num: [
          {required: true, message: '产品数量不能为空'},
          {type: 'number', message: '产品数量必须为数字值'}
        ],
        contact: [
          {required: true, message: '请填写联系人'},
        ],
        phone: [
          {required: true, message: '请填写联系人电话'},
        ],
      }
    }
  },
  created() {
    this.getList();
  },
  filters: {
    statusFilter(val) {
      return statusEnums.find(s => s.type === val).value;
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
      this.$confirm('是否要删除该委外单信息?', '提示', {
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
    updateStatus(row, status) {
      this.$confirm('是否要更改委外单状态?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateStatus(row.id, status).then(response => {
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
        exportXmlOutSourceOrder(this.param).then(res => {
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

