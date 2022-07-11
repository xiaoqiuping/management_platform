<template>
  <div class="app-container">
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
        <el-form :inline="true" size="small" label-width="140px">
          <el-form-item>
            <el-input v-model="param.supplier_name" class="input-width" placeholder="供应商名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="cooperation_date"
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
    <div class="operate-container">
      <el-button size="primary" class="btn-add" @click="handleAdd()"><i class="el-icon-plus left_icon"></i>添加
      </el-button>
    </div>
    <div class="table-container">
      <el-table ref="adminTable"
                :data="list"
                style="width: 100%;"
                v-loading="listLoading" >
        <el-table-column label="供应商名称" align="center">
          <template slot-scope="scope">{{ scope.row.supplier_name }}</template>
        </el-table-column>
        <el-table-column label="供应商地址" align="center">
          <template slot-scope="scope">{{ scope.row.supplier_address }}</template>
        </el-table-column>
        <el-table-column label="供应商联系人" align="center">
          <template slot-scope="scope">{{ scope.row.contact }}</template>
        </el-table-column>
        <el-table-column label="联系人职务" align="center">
          <template slot-scope="scope">{{ scope.row.contact_duty }}</template>
        </el-table-column>
        <el-table-column label="联系人电话" align="center">
          <template slot-scope="scope">{{ scope.row.contact_phone }}</template>
        </el-table-column>
        <el-table-column label="合作日期" align="center">
          <template slot-scope="scope">{{ scope.row.cooperation_date }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleQueryMaterial(scope.$index, scope.row)">
              查看材料
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
    <el-dialog
      :title="isEdit?'编辑供应商':'添加供应商'"
      :visible.sync="dialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="供应商名称：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.supplier_name" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="供应商地址：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.supplier_address"
                    type="textarea"
                    maxlength="300"
                    :rows="2"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="供应商联系人：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.contact" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="联系人职务：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.contact_duty" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="联系人电话：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.contact_phone" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="合作日期：" style="margin-bottom: 10px">
          <el-date-picker
            v-model="mainEntry.cooperation_date"
            type="date"
            align="right"
            value-format="yyyy-MM-dd" style="width: 350px">
          </el-date-picker>
        </el-form-item>

        <el-form-item
          label="供应材料1"
          style="margin-bottom: 10px">
          <el-row>
            <el-col :span="7">
              <el-input v-model="mainEntry.materials[0].material_name" placeholder="材料名称"></el-input>
            </el-col>
            <el-col :span="7">
              <el-input v-model="mainEntry.materials[0].material_price" placeholder="材料价格"></el-input>
            </el-col>
            <el-col :span="3">
              <el-button @click="addMaterial()" style="margin-left: 10px">新增</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item
          v-for="(material, index) in mainEntry.materials"
          :label="'供应材料' + (index+1)"
          :key="index"
          style="margin-bottom: 10px" v-if="index!==0">
          <el-row>
            <el-col :span="7">
              <el-input v-model="material.material_name" placeholder="材料名称"></el-input>
            </el-col>
            <el-col :span="7">
              <el-input v-model="material.material_price" placeholder="材料价格"></el-input>
            </el-col>
            <el-col :span="3">
              <el-button @click="addMaterial()" style="margin-left: 10px">新增</el-button>
            </el-col>
            <el-col :span="3">
              <el-button @click.prevent="removeMaterial(material)">删除</el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item label="备注：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.remarks"
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm()" size="small">确 定</el-button>
      </span>
    </el-dialog>



    <el-dialog
      :title="this.mainEntry.supplier_name+'的供应材料'"
      :visible.sync="xqDialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               label-width="150px" size="small">
        <el-form-item
          v-for="(material, index) in mainEntry.materials"
          :label="'供应材料' + (index+1)"
          :key="index"
          style="margin-bottom: 10px">
          <el-row>
            <el-col :span="7">
              <el-input v-model="material.material_name" placeholder="材料名称" disabled></el-input>
            </el-col>
            <el-col :span="7">
              <el-input v-model="material.material_price" placeholder="材料价格" disabled></el-input>
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
import {listPage, save, remove, update,get} from '@/api/backstage/supplier'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  supplier_name: null,
  cooperation_start_date: null,
  cooperation_end_date: null
}
const defaultEntry = {
  supplier_name: null,
  supplier_address: null,
  contact: null,
  contact_duty: null,
  contact_phone: null,
  cooperation_date: null,
  remarks: null,
  materials: [
    {
      material_name:null,
      material_price:null
    }
  ]
}
export default {
  name: "SupplierList",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      cooperation_date: null,
      list: null,
      listLoading: false,
      dialogVisible: false,
      xqDialogVisible:false,
      isEdit: false,
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleResetSearch() {
      this.page = Object.assign({}, defaultPage);
      this.param = Object.assign({}, defaultParam);
      this.cooperation_date = null;
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
      if(this.cooperation_date){
        this.param.cooperation_start_date=this.cooperation_date[0]
        this.param.cooperation_end_date=this.cooperation_date[1]
      }else {
        this.param.cooperation_start_date=null;
        this.param.cooperation_end_date=null;
      }
      listPage(this.param, this.page).then(response => {
        this.listLoading = false;
        this.list = response.data.content;
        this.page.total = response.data.totalElements;
      });
    },
    // 添加（唤起dialog）
    handleAdd() {
      this.dialogVisible = true;
      this.isEdit = false;
      this.mainEntry = Object.assign({}, defaultEntry);
    },

    //详情
    handleQueryMaterial(index,row) {
      this.listLoading = true;
      get(row.id).then(response => {
        this.listLoading = false;
        this.mainEntry = response.data;
      });
      this.xqDialogVisible = true
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该供应商?', '提示', {
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
    //更新（唤起dialog）
    handleUpdate(index, row) {
      this.dialogVisible = true;
      this.isEdit = true;
      this.mainEntry = Object.assign({}, row);
    },
    //保存或更新信息
    handleDialogConfirm() {
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
    },
    removeMaterial(item) {
      var index = this.mainEntry.materials.indexOf(item)
      if (index !== -1) {
        this.mainEntry.materials.splice(index, 1)
      }
    },
    addMaterial() {
      this.mainEntry.materials.push({
        material_name:null,
        material_price:null,
      });
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>
