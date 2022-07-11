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
            <el-input v-model="param.duty_man" class="input-width" placeholder="领料人" clearable></el-input>
          </el-form-item>
<!--          <el-form-item>-->
<!--            <el-input v-model="param.operation_time" class="input-width" placeholder="领料时间" clearable></el-input>-->
<!--          </el-form-item>-->
        </el-form>
      </div>
    </el-card>
    <!--操作栏-->
    <div class="operate-container">
      <el-button size="primary" class="btn-add" @click="handleAdd()"><i class="el-icon-plus left_icon"></i>新增
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
        <el-table-column label="单号" align="center">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="领料人" align="center">
          <template slot-scope="scope">{{ scope.row.duty_man }}</template>
        </el-table-column>
        <el-table-column label="领料时间" align="center">
          <template slot-scope="scope">{{ scope.row.operation_time }}</template>
        </el-table-column>
        <el-table-column label="操作类型" align="center">
          <template slot-scope="scope">{{ scope.row.operation_type |operationTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="操作人" align="center">
          <template slot-scope="scope">{{ scope.row.operation_man }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{ scope.row.remarks }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleQueryMaterial(scope.$index, scope.row)">
              领料详情
            </el-button>
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
        :total="total_num">
      </el-pagination>
    </div>
    <!--添加编辑页面-->
    <el-dialog
      :title="isEdit?'编辑领料信息':'新增领料信息'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="操作类型：" class="marginBottom22">领料</el-form-item>
        <el-form-item label="领料人：" class="marginBottom22" prop="duty_man">
          <el-input v-model="mainEntry.duty_man" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="领料时间：" class="marginBottom22" prop="operation_time">
          <el-date-picker
            v-model="mainEntry.operation_time"
            type="date"
            align="right"
            value-format="yyyy-MM-dd" class="width450">
          </el-date-picker>
        </el-form-item>
        <el-form-item v-if="!isEdit"
          label="物料1"
          class="marginBottom22">
          <el-row>
            <el-col :span="7">
              <el-select
                v-model="chooseMaterialList[0]"
                value-key="id"
                @change="changeChoose(0)"
                filterable
                remote
                reserve-keyword
                placeholder="请输入物料名称"
                :remote-method="getMaterialList"
                :loading="listLoading">
                <el-option
                  v-for="item in materialList"
                  :key="item.id+''"
                  :label="`${(item.material_name)}/${(item.material_model)}`"
                  :value="item">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <el-input v-model="mainEntry.material_operation_list[0].operation_num" placeholder="物料数量"></el-input>
            </el-col>
            <el-col :span="3">
              <el-button @click="addMaterialOp()" style="margin-left: 10px">新增</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item
          v-for="(material, index) in mainEntry.material_operation_list"
          :label="'物料' + (index+1)"
          :key="index"
          class="marginBottom22" v-if=" !isEdit && index!==0">
          <el-row>
            <el-col :span="7">
              <el-select
                v-model="chooseMaterialList[index]"
                @change="changeChoose(index)"
                value-key="id"
                filterable
                remote
                reserve-keyword
                placeholder="请输入物料名称"
                :remote-method="getMaterialList"
                :loading="listLoading">
                <el-option
                  v-for="item in materialList"
                  :key="item.id+''"
                  :label="`${(item.material_name)}/${(item.material_model)}`"
                  :value="item">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              <el-input v-model="mainEntry.material_operation_list[index].operation_num" placeholder="物料数量"></el-input>
            </el-col>
            <el-col :span="3">
              <el-button @click="addMaterialOp()" style="margin-left: 10px">新增</el-button>
            </el-col>
            <el-col :span="3">
              <el-button @click.prevent="removeMaterial(material)">删除</el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item v-if="isEdit"
                      v-for="(material, index) in mainEntry.material_operation_list"
                      :label="'物料' + (index+1)"
                      :key="index"
                      class="marginBottom22">

          <el-row>
            <el-col :span="7">
              <el-input :value="`${(material.material_name)}/${(material.material_model)}`" placeholder="材料名称/型号"
                        disabled></el-input>
            </el-col>
            <el-col :span="7">
              <el-input v-model="mainEntry.material_operation_list[index].operation_num" placeholder="物料数量"></el-input>
            </el-col>
          </el-row>
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
    <!--    领料详情-->
    <el-dialog
      :title="this.mainEntry.duty_man+'的领料详情'"
      :visible.sync="xqDialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               label-width="150px" size="small">
        <el-form-item
          v-for="(material, index) in mainEntry.material_operation_list"
          :label="'物料' + (index+1)"
          :key="index"
          class="marginBottom22">
          <el-row>
            <el-col :span="7">
              <el-input :value="`${(material.material_name)}/${(material.material_model)}`" placeholder="材料名称/型号"
                        disabled></el-input>
            </el-col>
            <el-col :span="7">
              <el-input :value="material.operation_num" placeholder="数量" disabled></el-input>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="xqDialogVisible= false" size="small">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listPage, save, remove, update, get} from '@/api/warehouse_storage/material_order'
import {listAllByMaterialName} from '@/api/warehouse_storage/material'


const defaultPage = {
  page: 1,
  size: 10,
}
const defaultParam = {
  operation_type: 1,
  duty_man: null,
  operation_time: null,
}
const defaultEntry = {
  id: null,
  order_no: null,
  duty_man: null,
  operation_time: null,
  operation_type: 1,
  operation_man: null,
  remarks: null,
  material_operation_list: [
    {
      material_id:null,
      material_name: null,
      material_model: null,
      material_type: null,
      children_type: null,
      operation_num: null,
    }
  ]
}
const chooseMaterialList = []

export default {
  name: "ProductionPicking",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      xqDialogVisible: false,
      isEdit: false,
      materialList: null,
      total_num: 0,
      chooseMaterialList,
      rules: {
        duty_man: [
          {required: true, message: '领料人不能为空'},
        ],
        operation_time: [
          {required: true, message: '领料时间不能为空'},
        ],
      }
    }
  },
  created() {
    this.getList();
  },
  filters: {
    operationTypeFilter(val) {
      if (val === 1) {
        return "领料";
      } else if (val === 2) {
        return "退料"
      } else {
        return "补料"
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
    getMaterialList(query) {
      if (query && query !== '') {
        listAllByMaterialName({material_name: query}).then(response => {
          this.materialList = response.data;
        })
      } else {
        this.materialList = [];
      }
    },
    //详情
    handleQueryMaterial(index, row) {
      this.xqDialogVisible = true
      get(row.id).then(response => {
        this.mainEntry = response.data;
      });
    },
    handleReset() {
      this.getList();
      this.getMaterialList();
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
    handleClose(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
      this.chooseMaterialList = []
      this.mainEntry.material_operation_list = [
        {
          material_id: null,
          material_name: null,
          material_model: null,
          material_type: null,
          children_type: null,
          operation_num: null,
        }
      ]
    },
    //保存或更新信息
    handleDialogConfirm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.chooseMaterialList.length)
          if (this.chooseMaterialList.length === 0) {
            this.$message({
              message: '请至少选择一种物料！',
              type: 'warning'
            });
            return;
          }
          for (let i = 0; i < this.chooseMaterialList.length; i++) {
            if(this.chooseMaterialList[i]){
              this.mainEntry.material_operation_list[i].material_id = this.chooseMaterialList[i].id;
              this.mainEntry.material_operation_list[i].material_name = this.chooseMaterialList[i].material_name;
              this.mainEntry.material_operation_list[i].material_model = this.chooseMaterialList[i].material_model;
              this.mainEntry.material_operation_list[i].material_type = this.chooseMaterialList[i].material_type;
              this.mainEntry.material_operation_list[i].children_type = this.chooseMaterialList[i].children_type;
            }
          }
          for (let i = 0; i < this.mainEntry.material_operation_list.length; i++) {
            if(!this.mainEntry.material_operation_list[i].material_name){
              this.$message({
                message: '物料'+(i+1)+'不能为空',
                type: 'warning'
              });
              return;
            }
            if(!this.mainEntry.material_operation_list[i].operation_num){
              this.$message({
                message: '物料'+(i+1)+'数量不能为空',
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
            if (this.isEdit) {
              update(this.mainEntry).then(response => {
                this.$message({
                  message: '修改成功！',
                  type: 'success'
                });
                this.dialogVisible = false;
                this.getList();
                this.chooseMaterialList = []
                this.mainEntry.material_operation_list = [
                  {
                    material_id: null,
                    material_name: null,
                    material_model: null,
                    material_type: null,
                    children_type: null,
                    operation_num: null,
                  }
                ]
              })
            } else {
              save(this.mainEntry).then(response => {
                this.$message({
                  message: '添加成功！',
                  type: 'success'
                });
                this.dialogVisible = false;
                this.getList();
                this.chooseMaterialList = []
                this.mainEntry.material_operation_list = [
                  {
                    material_id: null,
                    material_name: null,
                    material_model: null,
                    material_type: null,
                    children_type: null,
                    operation_num: null,
                  }
                ]
              })
            }
          })
        }
      })
    },
    changeChoose(index) {
      this.chooseMaterialList.splice(index, 1, this.chooseMaterialList[index])
    },
    removeMaterial(item) {
      var index = this.mainEntry.material_operation_list.indexOf(item)
      if (index !== -1) {
        this.mainEntry.material_operation_list.splice(index, 1)
        this.chooseMaterialList.splice(index, 1)
      }
    },
    addMaterialOp() {
      this.mainEntry.material_operation_list.push({
        material_id:null,
        material_name: null,
        material_model: null,
        material_type: null,
        children_type: null,
        operation_num: null,
      });
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该领料信息?', '提示', {
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

