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
            <el-input v-model="param.raw_material_name" class="input-width" placeholder="原料名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.operation_type" clearable placeholder="请选择操作类型" @clear="param.operation_type=null">
              <el-option
                v-for="operationType in operationTypeEnum"
                :key="operationType.value"
                :label="operationType.value"
                :value="operationType.type">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <!--操作栏-->
    <div class="operate-container">
      <el-button size="primary" class="btn-add" @click="handleAdd(0)"><i class="el-icon-plus left_icon"></i>入库
      </el-button>
      <el-button size="primary" class="btn-add" @click="handleAdd(1)"><i class="el-icon-minus left_icon"></i>出库
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
        <el-table-column label="原料名称" align="center">
          <template slot-scope="scope">{{ scope.row.raw_material_name }}</template>
        </el-table-column>
        <el-table-column label="原料价格" align="center">
          <template slot-scope="scope">{{ scope.row.raw_material_price }}</template>
        </el-table-column>
        <el-table-column label="数量" align="center">
          <template slot-scope="scope">{{ scope.row.operation_num }}</template>
        </el-table-column>
        <el-table-column label="操作类型" align="center">
          <template slot-scope="scope">{{ scope.row.operation_type | operationTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="操作时间" align="center">
          <template slot-scope="scope">{{ scope.row.operation_time  }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{ scope.row.remarks }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
<!--            <el-button size="mini"-->
<!--                       type="text"-->
<!--                       @click="handleUpdate(scope.$index, scope.row)">-->
<!--              编辑-->
<!--            </el-button>-->
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
        :total="total_num">
      </el-pagination>
    </div>
    <!--添加编辑页面-->
    <el-dialog
      :title="isEdit?'编辑原料信息':'新增原料信息'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="原料名称：" class="marginBottom22" prop="raw_material_name">
          <el-select v-model="mainEntry.raw_material_name" clearable placeholder="请选择" class="width450" v-if="!isEdit">
            <el-option
              v-for="item in materialList"
              :key="item.id+''"
              :value="item.material_name">
            </el-option>
          </el-select>
          <el-input v-model="mainEntry.raw_material_name" class="width450" v-if="isEdit" disabled></el-input>
        </el-form-item>
        <el-form-item label="现库存量：" class="marginBottom22" v-if="!isEdit && mainEntry.raw_material_name">
          {{chooseMaterial.total_num}}
        </el-form-item>
        <el-form-item label="原料价格：" class="marginBottom22">
          <el-input v-model="mainEntry.raw_material_price" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="数量：" class="marginBottom22" prop="operation_num">
          <el-input v-model.number="mainEntry.operation_num" class="width450" v-show="!isEdit"></el-input>
          <el-input v-model.number="mainEntry.operation_num" class="width450" v-show="isEdit" disabled></el-input>
        </el-form-item>
        <el-form-item label="操作类型：" class="marginBottom22">
          <span v-show="mainEntry.operation_type===0">入库</span>
          <span v-show="mainEntry.operation_type===1">出库</span>
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
import {listPage, save, remove, update} from '@/api/warehouse_storage/raw_material'
import {listAllByType} from '@/api/warehouse_storage/material'

const defaultPage = {
  page: 1,
  size: 10,
}
const defaultParam = {
  raw_material_name: null,
  operation_type: null,
}
const defaultEntry = {
  id: null,
  raw_material_name: null,
  material_id: null,
  raw_material_price: null,
  operation_num: null,
  operation_type: null,
  operation_time: null,
  duty_man: null,
  remarks: null,
};
const operationTypeEnum =[
  {
    type:0,
    value:"入库"
  },
  {
    type:1,
    value:"出库"
  }
]

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
      operationTypeEnum,
      total_num:0,
      rules: {
        raw_material_name: [
          {required: true, message: '商品名称不能为空',trigger:'change'},
        ],
        operation_num: [
          {required: true, message: '数量不能为空'},
          {type: 'number', message: '数量必须为数字值'}
        ],
      }
    }
  },
  created() {
    this.getList();
    this.getMaterialList();
  },
  filters: {
    operationTypeFilter(val) {
      return val === 0 ? "入库" : "出库"
    }
  },
  computed: {
    // 计算属性的 getter
    chooseMaterial: function () {
      return this.materialList.find(v => {
        return v.material_name === this.mainEntry.raw_material_name
      });
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
    getMaterialList() {
      this.listLoading = true;
      listAllByType({materialType: 1}).then(response => {
        this.listLoading = false;
        this.materialList = response.data;
      });
    },
    handleReset() {
      this.getList();
      this.getMaterialList();
    },
    // 添加（唤起dialog）
    handleAdd(type) {
      this.dialogVisible = true;
      this.isEdit = false;
      this.mainEntry = Object.assign({}, defaultEntry);
      this.mainEntry.operation_type = type
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
              this.mainEntry.material_id = this.chooseMaterial.id;
              save(this.mainEntry).then(response => {
                this.$message({
                  message: '添加成功！',
                  type: 'success'
                });
                this.dialogVisible = false;
                this.getList();
                this.getMaterialList();
              })
            }
          })
        }
      })
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该原料信息?', '提示', {
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

