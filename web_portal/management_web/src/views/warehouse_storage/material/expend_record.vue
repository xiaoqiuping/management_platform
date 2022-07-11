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
            <el-input v-model="param.material_name" class="input-width" placeholder="物料名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.material_model" class="input-width" placeholder="物料型号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.tu_hao" class="input-width" placeholder="图号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-cascader :options="materialType" v-model="chooseType" clearable placeholder="物料类别"></el-cascader>
          </el-form-item>
          <el-form-item style="margin-bottom: 10px">
            <el-date-picker
              v-model="param.record_date_list"
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
                v-loading="listLoading">
        <el-table-column label="物料名称" align="center">
          <template slot-scope="scope">{{ scope.row.material_name }}</template>
        </el-table-column>
        <el-table-column label="物料型号" align="center">
          <template slot-scope="scope">{{ scope.row.material_model |materialModelFilter }}</template>
        </el-table-column>
        <el-table-column label="图号" align="center">
          <template slot-scope="scope">{{ scope.row.tu_hao }}</template>
        </el-table-column>
        <el-table-column label="装配数量" align="center">
          <template slot-scope="scope">{{ scope.row.assemble_num }}</template>
        </el-table-column>
        <el-table-column label="损耗数量" align="center">
          <template slot-scope="scope">{{ scope.row.loss_num}}</template>
        </el-table-column>
        <el-table-column label="报废数量" align="center">
          <template slot-scope="scope">{{ scope.row.scrap_num }}</template>
        </el-table-column>
        <el-table-column label="记录时间" align="center">
          <template slot-scope="scope">{{ scope.row.record_date }}</template>
        </el-table-column>
        <el-table-column label="登记人" align="center">
          <template slot-scope="scope">{{ scope.row.record_man }}</template>
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
        :total="total_page">
      </el-pagination>
    </div>
    <!--添加编辑页面-->
    <el-dialog
      :title="isEdit?'编辑物料信息':'新增物料信息'"
      :visible.sync="dialogVisible"
      @close="handleClose('materialForm')"
      width="40%">
      <el-form :model="mainEntry"
               ref="materialForm"
               :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="物料类别：" class="marginBottom22"  v-if="!isEdit">
          <el-cascader :options="materialType" v-model="chooseType" ref="modelCascader" clearable @change="changeMaterial()"
                       class="width450"></el-cascader>
        </el-form-item>

        <el-form-item label="物料名称/型号：" class="marginBottom22" prop="material_id"  v-if="!isEdit" >
          <el-select v-model="chooseMaterial" @change="handleChangeMaterial()" clearable placeholder="请选择" value-key="id" class="width450">
            <el-option v-if="!isEdit"
                       v-for="item in materialList"
                       :key="item.id+''"
                       :label="`${(item.material_name)}/${(item.material_model)}`"
                       :value="item">
            </el-option>
            <el-option v-if="isEdit" disabled
                       v-for="item in materialList"
                       :key="item.id+''"
                       :label="`${(item.material_name)}/${(item.material_model)}`"
                       :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="物料名称/型号：" class="marginBottom22" v-if="isEdit">
          <el-input v-model="mainEntry.material_name+'/'+mainEntry.material_model" class="width450" disabled></el-input>
        </el-form-item>

        <el-form-item label="装配数量：" class="marginBottom22">
          <el-input v-model="mainEntry.assemble_num" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="损耗数量：" class="marginBottom22">
          <el-input v-model="mainEntry.loss_num" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="报废数量：" class="marginBottom22">
          <el-input v-model="mainEntry.scrap_num" class="width450"></el-input>
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
        <el-button @click="handleClose('materialForm')" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm('materialForm')" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listPage, save, remove, update} from '@/api/warehouse_storage/expend_record'
import {materialType} from "@/const/materialType"
import {listAllByType} from '@/api/warehouse_storage/material'
import {exportXmlMaterialSummary} from '@/api/export/export'

const defaultPage = {
  page: 1,
  size: 10,
}

const defaultParam = {
  material_name: null,
  material_model: null,
  tu_hao: null,
  material_type: null,
  children_type: null,
  record_date_list:[],
}

const defaultEntry = {
  id: null,
  material_id:null,
  material_name: null,
  material_model: null,
  assemble_num: 0,
  loss_num: 0,
  scrap_num: 0,
  record_date: 0,
  record_man: null,
  remarks: null,
};



export default {
  name: "expend_record",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      total_page: 0,
      materialType,
      chooseType: [],
      materialList: null,
      chooseMaterial: null,
      rules: {
        material_id: [
          {required: true, message: '物料名称/型号不能为空'},
        ],
      }
    }
  },
  created() {
    this.getList();
  },
  filters: {
    materialTypeFilter(type) {
      return materialType.filter(v => v.value === type)[0].label
    },
    childrenTypeFilter(type) {
      let m = materialType.filter(v => v.value === type.material_type)[0];
      if (m.children) {
        return m.children.filter(c => c.value === type.children_type)[0].label
      } else {
        return "N/A"
      }
    },
    materialModelFilter(type) {
      return type ? type : "N/A"
    }
  },
  methods: {
    handleResetSearch() {
      this.page = Object.assign({}, defaultPage);
      this.param = Object.assign({}, defaultParam);
      this.chooseType = []
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
    handleClose(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
      this.chooseType = [];
    },
    //查询
    getList() {
      this.listLoading = true;
      this.param.material_type = this.chooseType[0];
      this.param.children_type = this.chooseType[1];
      listPage(this.param, this.page).then(response => {
        this.listLoading = false;
        this.list = response.data.content;
        this.total_page = response.data.totalElements;
      });
    },
    changeMaterial(){
      this.mainEntry.material_id = null;
      this.chooseMaterial = null;
      this.materialList = null;
      if(this.chooseType.length>0){
        listAllByType({materialType: this.chooseType[0],childrenType:this.chooseType[1]}).then(response => {
          this.materialList = response.data;
        });
      }
    },
    handleChangeMaterial() {
      this.mainEntry.material_id = this.chooseMaterial.id;
    },
    handleReset() {
      this.getList();
    },
    // 添加（唤起dialog）
    handleAdd() {
      this.dialogVisible = true;
      this.isEdit = false;
      this.mainEntry = Object.assign({}, defaultEntry);
      this.chooseType = []
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
          this.mainEntry.material_type = this.chooseType[0];
          if(!this.isEdit){
            if (!this.mainEntry.material_type) {
              this.$message({
                message: '请选择物料类型！',
                type: 'warning'
              });
              return;
            }
          }
          this.$confirm('是否确认保存?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.mainEntry.children_type = this.chooseType[1];
            if (this.isEdit) {
              update(this.mainEntry).then(response => {
                this.$message({
                  message: '修改成功！',
                  type: 'success'
                });
                this.dialogVisible = false;
                this.chooseType = [];
                this.chooseMaterial=null;
                this.getList();
              })
            } else {
              save(this.mainEntry).then(response => {
                this.$message({
                  message: '添加成功！',
                  type: 'success'
                });
                this.dialogVisible = false;
                this.chooseType = [];
                this.chooseMaterial=null;
                this.getList();
              })
            }
          })
        }
      })
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该物料信息?', '提示', {
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
    //导出数据
    handleExport() {
      this.$confirm('是否要导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if(this.chooseType &&this.chooseType.length>0){
          this.param.material_type = this.chooseType[0];
          if(this.chooseType[1]){
            this.param.children_type = this.chooseType[1]
          }
        }
        exportXmlMaterialSummary(this.param).then(res => {
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
