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
<!--          <el-form-item>-->
<!--            <el-input v-model="param.express_no" class="input-width" placeholder="快递单号" clearable></el-input>-->
<!--          </el-form-item>-->
          <el-form-item>
            <el-input v-model="param.production_name" class="input-width" placeholder="产品名称" clearable></el-input>
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
        <el-table-column label="发货单单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="产品名称" align="center">
          <template slot-scope="scope">{{ scope.row.production_name }}</template>
        </el-table-column>
        <el-table-column label="产品型号" align="center">
          <template slot-scope="scope">{{ scope.row.production_type }}</template>
        </el-table-column>
        <el-table-column label="数量" align="center">
          <template slot-scope="scope">{{ scope.row.total_num }}</template>
        </el-table-column>
        <el-table-column label="客户名称" align="center">
          <template slot-scope="scope">{{ scope.row.buyer }}</template>
        </el-table-column>
        <el-table-column label="客户电话" align="center">
          <template slot-scope="scope">{{ scope.row.buyer_phone }}</template>
        </el-table-column>
        <el-table-column label="客户地址" align="center">
          <template slot-scope="scope">{{ scope.row.buyer_address }}</template>
        </el-table-column>
        <el-table-column label="快递方式" align="center">
          <template slot-scope="scope">{{ scope.row.express_type }}</template>
        </el-table-column>
        <el-table-column label="状态" align="center">
          <template slot-scope="scope">{{ scope.row.status | statusFilter }}</template>
        </el-table-column>
        <el-table-column label="销售单单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.sale_order_no }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{ scope.row.remarks }}</template>
        </el-table-column>
        <el-table-column label="发货时间" align="center">
          <template slot-scope="scope">{{ scope.row.send_date }}</template>
        </el-table-column>
        <el-table-column label="发货单号" align="center">
          <template slot-scope="scope">{{ scope.row.express_no }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini" v-if="scope.row.status===1"
                       type="text"
                       @click="handleUpdate(scope.$index, scope.row)">
              编辑
            </el-button>
            <el-button size="mini" v-if="scope.row.status===1"
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
        :total="total">
      </el-pagination>
    </div>
    <!--添加编辑页面-->
    <el-dialog
      :title="isEdit?'编辑价目':'新增价目'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="产品名称/型号" class="marginBottom22" prop="production_name">
          <el-select v-model="chooseMaterial"
                     @change="handleChangeMaterial()"
                     placeholder="请选择产品" class="width450" v-if="!isEdit">
            <el-option
              v-for="(material) in materialList"
              :key="material.id+''"
              :label="material.name"
              :value="material.name">
            </el-option>
          </el-select>
          <el-input :value="`${(mainEntry.production_name)}/${(mainEntry.production_type)}`" disabled class="width450" v-if="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="数量" class="marginBottom22" width="220px" prop="total_num">
          <el-input v-model.number="mainEntry.total_num" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="销售单单号" class="marginBottom22" prop="sale_order_no">
          <el-input v-model="mainEntry.sale_order_no" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="客户名称" class="marginBottom22" prop="buyer">
          <el-input v-model="mainEntry.buyer" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="客户电话" class="marginBottom22" width="220px" prop="buyer_phone">
          <el-input v-model="mainEntry.buyer_phone" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="客户地址" class="marginBottom22" prop="buyer_address">
          <el-input v-model="mainEntry.buyer_address"
                    type="textarea"
                    maxlength="100"
                    :rows="2"
                    show-word-limit
                    class="width450"></el-input>
        </el-form-item>
        <el-form-item label="快递方式" class="marginBottom22" prop="express_type">
          <el-input v-model="mainEntry.express_type" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="备注" class="marginBottom22">
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
import {listPage, save, remove, update} from '@/api/sale/sendGoodsOrder'
import {sendGoodsOrderStatus} from "@/const/sendGoodsOrder"
import {listAllByType} from '@/api/warehouse_storage/material'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  express_no: null,
  production_name: null,
}
const defaultEntry = {
  id: null,
  order_no: null,
  production_name: null,
  total_num: null,
  sale_order_no: null,
  buyer: null,
  buyer_phone: null,
  buyer_address: null,
  express_type: null,
  sender: null,
  send_date: null,
  express_no: null,
  status: null,
  remarks: null,

};
export default {
  name: "SendGoodsOrder",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      total: null,
      sendGoodsOrderStatus,
      materialList:[],
      chooseMaterial:null,
      rules: {
        production_name: [
          {required: true, message: '产品名称/型号不能为空'},
        ],
        total_num: [
          {required: true, message: '数量不能为空'},
          {type: 'number', message: '数量必须为数字值'}
        ],
        sale_order_no: [
          {required: true, message: '销售单单号不能为空'},
        ],
        buyer: [
          {required: true, message: '客户名称不能为空'},
        ],
        buyer_phone: [
          {required: true, message: '客户电话不能为空'},
        ],
        buyer_address: [
          {required: true, message: '客户地址不能为空'},
        ],
        express_type: [
          {required: true, message: '快递方式不能为空'},
        ],
      }
    }
  },
  created() {
    this.getList();
    this.getMaterialList();
  },
  filters:{
    statusFilter(status){
      if(!status){
        return "N/A"
      }
      return sendGoodsOrderStatus.find(r => r.type === status).value;
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
        this.total = response.data.totalElements;
      });
    },
    getMaterialList() {
      this.listLoading = true;
      listAllByType({materialType: 3,childrenType:1}).then(response => {
        this.listLoading = false;
        let temp = response.data;
        temp.forEach(t=>{
          this.materialList.push({id:t.id,name:t.material_name+"/"+t.material_model})
        })
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
    handleChangeMaterial() {
      let split = this.chooseMaterial.split("/")
      this.mainEntry.production_name = split[0];
      this.mainEntry.production_type = split[1];
    },
    handleClose(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
      this.chooseMaterial = null
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
                this.chooseMaterial = null;
              })
            } else {
              save(this.mainEntry).then(response => {
                this.$message({
                  message: '添加成功！',
                  type: 'success'
                });
                this.dialogVisible = false;
                this.getList();
                this.chooseMaterial = null;
              })
            }
          })
        }
      })
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该发货申请单?', '提示', {
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
