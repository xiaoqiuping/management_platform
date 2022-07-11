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
                v-loading="listLoading">
        <el-table-column label="物料类别" align="center">
          <template slot-scope="scope">{{ scope.row.material_type | materialTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="子类别" align="center">
          <template slot-scope="scope">{{ scope.row | childrenTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="物料名称" align="center">
          <template slot-scope="scope">{{ scope.row.material_name }}</template>
        </el-table-column>
        <el-table-column label="物料型号" align="center">
          <template slot-scope="scope">{{ scope.row.material_model |materialModelFilter }}</template>
        </el-table-column>
        <el-table-column label="图号" align="center">
          <template slot-scope="scope">{{ scope.row.tu_hao }}</template>
        </el-table-column>
        <el-table-column label="库存量" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.total_num<1000" style="color: red">{{ scope.row.total_num }}</span>
            <span v-if="scope.row.total_num>=1000" style="color: #1a9a0c">{{ scope.row.total_num }}</span>
          </template>
        </el-table-column>
        <el-table-column label="单价" align="center">
          <template slot-scope="scope">{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column label="总金额" align="center">
          <template slot-scope="scope">{{ scope.row.total_num * scope.row.price }}</template>
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
        <el-form-item label="物料类别：" class="marginBottom22">
          <el-cascader :options="materialType" v-model="chooseType" ref="modelCascader" clearable v-if="!isEdit"
                       class="width450"></el-cascader>
          <el-cascader :options="materialType" v-model="chooseType" ref="modelCascader" clearable v-if="isEdit" disabled
                       class="width450"></el-cascader>
        </el-form-item>
        <el-form-item label="物料名称：" class="marginBottom22" prop="material_name">
          <el-input v-model="mainEntry.material_name" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="物料型号：" class="marginBottom22" v-if="chooseType[0] && this.chooseType[0]!==1"
                      prop="material_model">
          <el-input v-model="mainEntry.material_model" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="图号：" class="marginBottom22" v-if="chooseType[0] && this.chooseType[0]!==1">
          <el-input v-model="mainEntry.tu_hao" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="价格：" class="marginBottom22">
          <el-input v-model="mainEntry.price" class="width450"></el-input>
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
import {listPage, save, remove, update} from '@/api/warehouse_storage/material'
import {materialType} from "@/const/materialType"

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
}
const defaultEntry = {
  id: null,
  material_name: null,
  material_model: null,
  material_type: null,
  children_type: null,
  total_num: 0,
  price: 0,
  tu_hao: null,
  remarks: null,
};
export default {
  name: "Material",
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
      rules: {
        material_type: [
          {required: true, message: '物料类型不能为空', trigger: 'change'}
        ],
        material_name: [
          {required: true, message: '物料名称不能为空'}
        ],
        material_model: [
          {required: true, message: '物料型号不能为空'}
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
      this.chooseType = []
      this.dialogVisible = true;
      this.isEdit = true;
      this.chooseType.push(row.material_type)
      this.chooseType.push(row.children_type)
      this.mainEntry = Object.assign({}, row);
    },
    //保存或更新信息
    handleDialogConfirm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.mainEntry.material_type = this.chooseType[0];
          if (!this.mainEntry.material_type) {
            this.$message({
              message: '请选择物料类型！',
              type: 'warning'
            });
            return;
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
                this.chooseType = []
                this.getList();
              })
            } else {
              save(this.mainEntry).then(response => {
                this.$message({
                  message: '添加成功！',
                  type: 'success'
                });
                this.dialogVisible = false;
                this.chooseType = []
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
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>

