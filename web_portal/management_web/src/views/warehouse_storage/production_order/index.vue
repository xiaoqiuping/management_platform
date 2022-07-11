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
            <el-input v-model="param.production_type" class="input-width" placeholder="产品型号" clearable></el-input>
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
        <el-table-column label="生产订单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="类型" align="center">
          <template slot-scope="scope">{{ scope.row.type | typeFilter }}</template>
        </el-table-column>
        <el-table-column label="产品名称" align="center">
          <template slot-scope="scope">{{ scope.row.production_name }}</template>
        </el-table-column>
        <el-table-column label="产品型号" align="center">
          <template slot-scope="scope">{{ scope.row.production_type }}</template>
        </el-table-column>
        <el-table-column label="生产总数量" align="center">
          <template slot-scope="scope">{{ scope.row.total_num }}</template>
        </el-table-column>
        <el-table-column label="单价" align="center">
          <template slot-scope="scope">{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column label="总金额" align="center">
          <template slot-scope="scope">{{ scope.row.total_price }}</template>
        </el-table-column>
        <el-table-column label="订单周期" align="center">
          <template slot-scope="scope">{{ scope.row.order_cycle }}</template>
        </el-table-column>
        <el-table-column label="订单状态" align="center">
          <template slot-scope="scope">{{ scope.row.status | orderStatusFilter }}</template>
        </el-table-column>
        <el-table-column label="订单周期" align="center">
          <template slot-scope="scope">{{ scope.row.order_cycle }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">

            <el-button size="mini" v-if="scope.row.status===10"
                       type="text"
                       @click="storageScOrder(scope.row,11)">
              入库
            </el-button>

            <el-button size="mini"
                       type="text"
                       @click="handleDrawer(scope.$index, scope.row)">
              详情
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
        :total="page.total">
      </el-pagination>
    </div>
    <!--添加编辑页面-->
    <el-dialog
      :title="isEdit?'编辑生产订单':'新增生产订单'"
      :visible.sync="dialogVisible"
      @close="handleClose('productionForm')"
      width="40%">
      <el-form :model="mainEntry"
               ref="productionForm" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="类型：" class="marginBottom22" prop="type">
          <el-radio-group v-model="mainEntry.type">
            <el-radio :label="1" border @change="handleChangeType(3)">成品</el-radio>
            <el-radio :label="2" border @change="handleChangeType(2)">零件</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="产品名称/型号" class="marginBottom22" prop="production_name">
          <el-select v-model="chooseMaterial" placeholder="请选择产品" class="width450" v-if="!isEdit"
                     @change="handleChangeMaterial()">
            <el-option
              v-for="(material) in materialList"
              :key="material.id+''"
              :label="material.name"
              :value="material.name">
            </el-option>
          </el-select>
          <el-input :value="`${(mainEntry.production_name)}/${(mainEntry.production_type)}`" disabled
                    class="width450" v-if="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="生产总数量：" class="marginBottom22" prop="total_num">
          <el-input v-model.number="mainEntry.total_num" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="单价：" class="marginBottom22" prop="price">
          <el-input v-model.number="mainEntry.price" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="总金额：" class="marginBottom22" prop="total_price">
          <el-input v-model.number="mainEntry.total_price"
                    class="width450"></el-input>
        </el-form-item>
        <el-form-item label="订单周期（天）：" class="marginBottom22" prop="order_cycle">
          <el-input v-model.number="mainEntry.order_cycle" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="完成日期：" class="marginBottom22" prop="production_finished_date">
          <el-date-picker
            v-model="mainEntry.production_finished_date"
            type="date"
            align="right"
            value-format="yyyy-MM-dd" class="width450">
          </el-date-picker>
        </el-form-item>

        <el-form-item
          label="工序步骤1"
          class="marginBottom22">
          <el-row>
            <el-col :span="7">
              <el-select v-model="mainEntry.process_list[0].process_name" clearable placeholder="请选择工序"
                         @change="changeChoose(0)"
                         @clear="mainEntry.process_list[0].process_name=null">
                <el-option
                  v-for="item in processList"
                  :key="item.id"
                  :label="item.process_name"
                  :value="item.process_name">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <el-button @click="addProcess()" style="margin-left: 10px">新增</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item
          v-for="(processData, index) in mainEntry.process_list"
          :label="'工序步骤' + (index+1)"
          :key="index"
          class="marginBottom22" v-if="index!==0">
          <el-row>
            <el-col :span="7">
              <el-select v-model="processData.process_name" clearable placeholder="请选择工序"
                         @change="changeChoose(index)"
                         @clear="processData.process_name=null">
                <el-option
                  v-for="item in processList"
                  :key="item.id"
                  :label="item.process_name"
                  :value="item.process_name">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="3">
              <el-button @click="addProcess()" style="margin-left: 10px">新增</el-button>
            </el-col>
            <el-col :span="3">
              <el-button @click.prevent="removeProcess(processData)">删除</el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <el-form-item label="发货形式：" class="marginBottom22" prop="send_goods_type" inline-message>
          <el-input v-model="mainEntry.send_goods_type" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="订单要求：" class="marginBottom22">
          <el-input v-model="mainEntry.order_requirement"
                    type="textarea"
                    maxlength="300"
                    :rows="3"
                    show-word-limit
                    class="width450"></el-input>
        </el-form-item>
        <el-form-item label="销售单号：" class="marginBottom22">
          <el-input v-model="mainEntry.sale_order_no" class="width450"></el-input>
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
        <el-button @click="handleClose('productionForm')" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm('productionForm')" size="small">确 定</el-button>
      </span>
    </el-dialog>

    <el-drawer
      title="我是标题"
      size="45%"
      :visible.sync="drawerVisible"
      :with-header="false">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>单号：{{ mainEntry.order_no }}</span>
        </div>
        <div style="height: 600px;overflow-y: auto">
          <el-form :model="mainEntry"
                   label-width="150px" size="small">
            <el-form-item label="类别：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.type | typeFilter }}</span>
            </el-form-item>
            <el-form-item label="产品名称：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.production_name }}</span>
            </el-form-item>
            <el-form-item label="产品型号：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.production_type }}</span>
            </el-form-item>
            <el-form-item label="生产总数量：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.total_num }}</span>
            </el-form-item>
            <el-form-item label="单价：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.price }}</span>
            </el-form-item>
            <el-form-item label="总金额：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.total_price }}</span>
            </el-form-item>
            <el-form-item label="订单周期（天）：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.order_cycle }}</span>
            </el-form-item>
            <el-form-item label="订单创建时间：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.order_create_time }}</span>
            </el-form-item>
            <el-form-item label="完成日期：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.production_finished_date }}</span>
            </el-form-item>
            <el-form-item label="发货形式：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.send_goods_type }}</span>
            </el-form-item>
            <el-form-item label="订单要求：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.order_requirement }}</span>
            </el-form-item>
            <el-form-item label="销售单号：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.sale_order_no }}</span>
            </el-form-item>
            <el-form-item label="备注：" class="marginBottom10">
              <span class="margintop10">{{ mainEntry.remarks }}</span>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>订单状态：{{ mainEntry.status | orderStatusFilter }}</span>
        </div>
        <el-steps :active="stepActive" align-center style="margin: 20px 0">
          <el-step
            :title="pro.process_name"
            :status="pro.quality_status|qualityStatusFilter"
            :description="`'质检数量'-${(pro.access_num?pro.access_num:0)}/${(pro.quality_num?pro.quality_num:0)}`"
            v-for="pro in mainEntry.process_list" :key="pro.process_step">{{ pro.quality_status|qualityStatusFilter }}
          </el-step>
        </el-steps>
      </el-card>
    </el-drawer>
  </div>
</template>

<script>
import {listPage, remove, save, update,examineOrder} from '@/api/warehouse_storage/production_order'
import {listAll} from '@/api/backstage/process'
import {scOrderStatus} from "@/const/SCOrderStatus"
import {exportXmlProductionOrder} from '@/api/export/export'
import {listAllByType} from '@/api/warehouse_storage/material'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  production_name: null,
  production_type: null,

}
const defaultEntry = {
  id: null,
  order_no: null,
  production_name: null,
  production_type: null,
  type: null,
  total_num: null,
  price: null,
  total_price: null,
  order_cycle: null,
  production_finished_date: null,
  send_goods_type: null,
  order_requirement: null,
  status: null,
  order_create_time: null,
  sale_order_no: null,
  remarks: null,
  process_list: [
    {
      process_step: null,
      process_name: null,
      process_id: null,
    }
  ],
};
export default {
  name: "ProductionOrder",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      rules: {
        type: [
          {required: true, message: '请选择产品类型', trigger: 'change'}
        ],
        production_name: [
          {required: true, message: '请选择产品名称/型号'},
        ],
        total_num: [
          {required: true, message: '生产数量不能为空'},
          {type: 'number', message: '生产数量必须为数字值'}
        ],
        price: [
          {required: true, message: '单价不能为空'},
          {type: 'number', message: '单价必须为数字值'}
        ],
        total_price: [
          {required: true, message: '总金额不能为空'},
          {type: 'number', message: '总金额必须为数字值'}
        ],
        order_cycle: [
          {required: true, message: '订单周期不能为空'},
          {type: 'number', message: '订单周期必须为数字值'}
        ],
        production_finished_date: [
          {required: true, message: '请选择日期', trigger: 'change'}
        ],
        send_goods_type: [
          {required: true, message: '请输入发货方式'},
        ],
      },
      list: null,
      listLoading: false,
      dialogVisible: false,
      drawerVisible: false,
      processList: null,
      isEdit: false,
      stepActive: 0,
      scOrderStatus,
      materialList: [],
      chooseMaterial: null,
    }
  },
  created() {
    this.getList();
    this.getProcessList();
  },
  filters: {
    orderStatusFilter(val) {
      if (!val) {
        return "N/A"
      }
      return scOrderStatus.find(o => o.value === val).label
    },
    qualityStatusFilter(status) {
      if (status == null) {
        return 'process';
      }
      return status === 1 ? 'success' : 'error';
    },
    typeFilter(type) {
      return type === 1 ? '成品' : '零件';
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
        this.page.total = response.data.totalElements;
      });
    },
    getProcessList() {
      listAll().then(response => {
        this.processList = response.data;
      });
    },
    handleChangeType(type) {
      this.materialList = []
      listAllByType({materialType: type}).then(response => {
        let temp = response.data;
        temp.forEach(t => {
          this.materialList.push({id: t.id, name: t.material_name + "/" + t.material_model})
        })
      });
    },
    handleChangeMaterial() {
      let split = this.chooseMaterial.split("/")
      this.mainEntry.production_name = split[0];
      this.mainEntry.production_type = split[1];
    },
    handleReset() {
      this.getList();
      this.getProcessList();
    },
    //唤起抽屉
    handleDrawer(index, row) {
      this.drawerVisible = true;
      this.mainEntry = Object.assign({}, row);
    },
    // 添加（唤起dialog）
    handleAdd() {
      this.dialogVisible = true;
      this.isEdit = false;
      this.chooseMaterial = null;
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
      this.chooseMaterial = null;
      this.materialList = [];
    },
    storageScOrder(entry,status) {
      Object.assign(this.mainEntry, entry);
      this.mainEntry.status = status;
      this.$confirm('是否确认入库?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        examineOrder(this.mainEntry).then(response => {
          this.$message({
            message: '入库成功！',
            type: 'success'
          });
          this.dialogVisible = false;
          this.getList();
        })
      })
    },
    //保存或更新信息
    handleDialogConfirm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.mainEntry.process_list.length > 0) {
            if (this.mainEntry.process_list[0].process_name == null) {
              this.$message({
                message: '请至少选择一个工序！',
                type: 'warning'
              });
              return;
            }
          }
          let process_list = this.mainEntry.process_list
          for (let i = 0; i < process_list.length; i++) {
            if(process_list[i].process_name){
              this.mainEntry.process_list[i].process_step = i + 1;
              this.mainEntry.process_list[i].process_id = this.processList.find(p => p.process_name === process_list[i].process_name).id;
            }else {
              this.$message({
                message: '工序'+(i+1)+'未选择，请移除',
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
      });
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该生产订单表?', '提示', {
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
    //验重
    changeChoose(index) {
      let temp = []
      Object.assign(temp, this.mainEntry.process_list)
      if (temp.length > 1) {
        let check = this.mainEntry.process_list[index]
        let number = temp.indexOf(check)
        temp.splice(number, 1);
        let find = temp.find(t => t.process_name === check.process_name)
        if (find) {
          this.$message({
            type: 'error',
            message: '已有该步骤，不能重复指定!'
          });
          let index = this.mainEntry.process_list.indexOf(check)
          this.mainEntry.process_list.splice(index, 1)
        }
      }
    },
    removeProcess(item) {
      let index = this.mainEntry.process_list.indexOf(item)
      if (index !== -1) {
        this.mainEntry.process_list.splice(index, 1)
      }
    },
    addProcess() {
      this.mainEntry.process_list.push({
        process_step: null,
        process_name: null,
        process_id: null,
      });
    },
    handleExport() {
      this.$confirm('是否要导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        exportXmlProductionOrder(this.param).then(res => {
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
  },

}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>

