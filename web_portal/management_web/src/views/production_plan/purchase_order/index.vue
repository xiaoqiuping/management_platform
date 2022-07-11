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
      <el-button size="primary" class="btn-add" @click="handleAdd(0)"><i class="el-icon-plus left_icon"></i>新增
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
                       @click="handleUpdate(scope.$index, scope.row)">
              编辑
            </el-button>
            <el-button size="mini"
                       type="text"
                       @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
            <el-button size="mini"
                       type="text"
                       @click="handleDrawer(scope.$index, scope.row)">详情
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
      :title="isEdit?'编辑采购单信息':'新增采购单信息'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="采购人：" class="marginBottom22" prop="purchase_man">
          <el-input v-model="mainEntry.purchase_man" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="采购时间：" class="marginBottom22" prop="purchase_date">
          <el-date-picker
            v-model="mainEntry.purchase_date"
            type="date"
            align="right"
            value-format="yyyy-MM-dd" class="width450">
          </el-date-picker>
        </el-form-item>
        <el-form-item
          label="原料1"
          class="marginBottom22">
          <el-row>
            <el-col :span="5">
              <el-select v-model="mainEntry.raw_material_list[0].raw_material_name" clearable placeholder="请选择原料"
                         @clear="mainEntry.raw_material_list=null">
                <el-option
                  v-for="m in materialList"
                  :key="m.material_name"
                  :value="m.material_name">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="5">
              <el-input v-model="mainEntry.raw_material_list[0].price" placeholder="原料价格"></el-input>
            </el-col>
            <el-col :span="5">
              <el-input v-model="mainEntry.raw_material_list[0].total_num" placeholder="采购数量"></el-input>
            </el-col>
            <el-col :span="3">
              <el-button @click="addMaterial()" style="margin-left: 10px">新增</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item
          v-for="(material, index) in mainEntry.raw_material_list"
          :label="'原料' + (index+1)"
          :key="index"
          class="marginBottom22" v-if="index!==0">
          <el-row>
            <el-col :span="5">
              <el-select v-model="material.raw_material_name" clearable placeholder="请选择原料"
                         @clear="material.raw_material_name=null">
                <el-option
                  v-for="m in materialList"
                  :key="m.material_name"
                  :value="m.material_name">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="5">
              <el-input v-model="material.price" placeholder="原料价格"></el-input>
            </el-col>
            <el-col :span="5">
              <el-input v-model="material.total_num" placeholder="采购数量"></el-input>
            </el-col>
            <el-col :span="3">
              <el-button @click="addMaterial()" style="margin-left: 10px">新增</el-button>
            </el-col>
            <el-col :span="3">
              <el-button @click.prevent="removeMaterial(material)">删除</el-button>
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
                          v-loading="listLoading">
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
import {listPage, save, remove, update} from '@/api/raw_materials_purchase/purchaseOrder'
import {listAllByType} from '@/api/warehouse_storage/material'
import {purchaseOrderStatus} from '@/const/purchaseOrder'

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
      purchaseOrderStatus,
      drawerVisible: false,
      rules: {
        purchase_man: [
          {required: true, message: '采购人不能为空'},
        ],
        purchase_date: [
          {required: true, message: '请选择采购日期'},
        ],
      }
    }
  },
  created() {
    this.getList();
    this.getMaterialList();
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
    handleClose(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
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
      this.mainEntry.operation_type = type;
      this.mainEntry.raw_material_list = [{
        raw_material_name: null,
        price: null,
        total_num: null,
      }]
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
          for (let i = 0; i < this.mainEntry.raw_material_list.length; i++) {
            if (this.mainEntry.raw_material_list[i].raw_material_name == null) {
              this.$message({
                message: '原料' + (i + 1) + '不能为空',
                type: 'warning'
              });
              return;
            }
            if (this.mainEntry.raw_material_list[i].price == null) {
              this.$message({
                message: '原料' + (i + 1) + '价格不能为空！',
                type: 'warning'
              });
              return;
            }
            if (this.mainEntry.raw_material_list[i].total_num == null) {
              this.$message({
                message: '原料' + (i + 1) + '数量不能为空',
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
              })
            } else {
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
      this.$confirm('是否要删除该原料采购单?', '提示', {
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
    addMaterial() {
      this.mainEntry.raw_material_list.push({
        raw_material_name: null,
        price: null,
        total_num: null,
      });
    },
    removeMaterial(item) {
      let index = this.mainEntry.raw_material_list.indexOf(item)
      if (index !== -1) {
        this.mainEntry.raw_material_list.splice(index, 1)
      }
    },
    //唤起抽屉
    handleDrawer(index, row) {
      console.log(row)
      this.drawerVisible = true;
      this.mainEntry = Object.assign({}, row);
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>
