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
            <el-input v-model="param.contract_name" class="input-width" placeholder="合同名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="upload_time"
              type="datetimerange"
              align="right"
              value-format="yyyy-MM-dd HH:mm:ss"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              :default-time="['00:00:00', '23:59:59']">
            </el-date-picker>
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
        <el-table-column label="合同名称" align="center">
          <template slot-scope="scope">{{ scope.row.contract_name }}</template>
        </el-table-column>
        <el-table-column label="供应商" align="center">
          <template slot-scope="scope">{{ scope.row.supplier }}</template>
        </el-table-column>
        <el-table-column label="合同金额" align="center">
          <template slot-scope="scope">{{ scope.row.total_amount }}</template>
        </el-table-column>
        <el-table-column label="上传人" align="center">
          <template slot-scope="scope">{{ scope.row.operator }}</template>
        </el-table-column>
        <el-table-column label="上传时间" align="center">
          <template slot-scope="scope">{{ scope.row.upload_time }}</template>
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
                       @click="handleContractFileDetails(scope.$index, scope.row)">附件详情
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
      :title="isEdit?'编辑采购合同信息':'新增采购合同信息'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="合同名称：" class="marginBottom22" prop="contract_name">
          <el-input v-model="mainEntry.contract_name" class="width450" v-if="!isEdit" ></el-input>
          <el-input v-model="mainEntry.contract_name" class="width450" v-if="isEdit" disabled></el-input>
        </el-form-item>
        <el-form-item label="供应商：" class="marginBottom22" prop="supplier">
          <el-input v-model="mainEntry.supplier" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="合同金额：" class="marginBottom22" prop="total_amount">
          <el-input v-model.number="mainEntry.total_amount" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="上传附件：" class="marginBottom22">
          <el-upload
            :action="dev?uploadUrl:'/management/minio/upload'"
            :headers="headers"
            :on-remove="handleRemove"
            :on-success="handleUploadSuccess"
            :before-remove="beforeRemove"
            multiple
            :limit="5"
            :on-exceed="handleExceed"
            :file-list="mainEntry.contract_file_list">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">文件大小不超过10Mb</div>
          </el-upload>
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
    <!--附件页面-->
    <el-dialog
      :title="'附件详情'"
      :visible.sync="cfDialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="合同名称：" class="marginBottom22">
          <el-input v-model="mainEntry.contract_name" class="width450" disabled></el-input>
        </el-form-item>
        <el-form-item label="附件列表：" class="marginBottom22">
          <el-table ref="adminTable"
                    :data="mainEntry.contract_file_list"
                    style="width: 100%;"
                    v-loading="listLoading" >
            <el-table-column label="附件名称" align="center">
              <template slot-scope="scope">{{ scope.row.name }}</template>
            </el-table-column>
            <el-table-column label="附件大小(Mb)" align="center">
              <template slot-scope="scope">{{ scope.row.size / 1024 / 1024 | formatNum }}</template>
            </el-table-column>
            <el-table-column label="操作" width="180" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handleDownload(scope.row)">下载</el-button>
                <el-button size="mini" type="text"><a :href="scope.row.url" target="view_window">预览</a></el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cfDialogVisible = false" size="small">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listPage, save, remove, update} from '@/api/raw_materials_purchase/purchaseContract'
import {uploadUrl, download} from '@/utils/minio'
import {getToken} from '@/utils/auth'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  contract_name: null,
  upload_start_time: null,
  upload_end_time: null,
}
const defaultEntry = {
  id: null,
  contract_name: null,
  supplier: null,
  total_amount: null,
  operator: null,
  upload_time: null,
  remarks: null,
  contract_file_list: []
};
export default {
  name: "PurchaseContractList",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      upload_time: null,
      headers: {
        "Authorization": getToken()
      },
      uploadUrl,
      cfDialogVisible: false,
      rules: {
        contract_name: [
          {required: true, message: '合同名称不能为空'},
        ],
        supplier: [
          {required: true, message: '供应商不能为空'},
        ],
        total_amount: [
          {required: true, message: '合同金额不能为空'},
          {type: 'number', message: '合同金额必须为数字值'}
        ],
      }
    }
  },
  created() {
    this.getList();
  },
  computed:{
    dev:function (){
      return process.env.NODE_ENV==='development'
    }
  },
  methods: {
    handleResetSearch() {
      this.page = Object.assign({}, defaultPage);
      this.param = Object.assign({}, defaultParam);
      this.upload_time = null;
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
      if (this.upload_time) {
        this.param.upload_start_time = this.upload_time[0]
        this.param.upload_end_time = this.upload_time[1]
      } else {
        this.param.upload_start_time = null;
        this.param.upload_end_time = null;
      }
      listPage(this.param, this.page).then(response => {
        this.listLoading = false;
        this.list = response.data.content;
        this.page.total = response.data.totalElements;
      });
    },
    //刷新
    handleReset() {
      this.getList();
    },
    handleDownload(fileData) {
      download(fileData);
    },
    //文件上传成功后
    handleUploadSuccess(file) {
      this.mainEntry.contract_file_list.push(file.data)
    },
    handleRemove(file, fileList) {
      var index = this.mainEntry.contract_file_list.indexOf(file)
      if (index !== -1) {
        this.mainEntry.contract_file_list.splice(index, 1)
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 5 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    // 添加（唤起dialog）
    handleAdd() {
      this.dialogVisible = true;
      this.isEdit = false;
      this.mainEntry = Object.assign({}, defaultEntry);
    },
    handleContractFileDetails(index, row) {
      this.cfDialogVisible = true
      this.mainEntry = Object.assign({}, row);
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
      this.$confirm('是否要删除该合同信息?', '提示', {
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
