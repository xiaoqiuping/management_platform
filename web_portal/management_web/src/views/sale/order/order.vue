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
            <el-input v-model="param.order_no" class="input-width" placeholder="单号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.watch_name" class="input-width" placeholder="手表名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.watch_type" class="input-width" placeholder="手表型号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.buyer" class="input-width" placeholder="购买商" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.salesman" class="input-width" placeholder="销售人" clearable></el-input>
          </el-form-item>
          <el-form-item style="margin-bottom: 10px">
            <el-date-picker
              v-model="sale_date"
              type="daterange"
              align="right"
              value-format="yyyy-MM-dd"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.status" clearable placeholder="请选择订单状态" @clear="param.status=null">
              <el-option
                v-for="orderStatus in orderStatusEnums"
                :key="orderStatus.type"
                :label="orderStatus.value"
                :value="orderStatus.type">
              </el-option>
            </el-select>
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
                v-loading="listLoading" >
        <el-table-column label="单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="手表名称" align="center">
          <template slot-scope="scope">{{ scope.row.watch_name }}</template>
        </el-table-column>
        <el-table-column label="手表型号" align="center">
          <template slot-scope="scope">{{ scope.row.watch_type }}</template>
        </el-table-column>
        <el-table-column label="销售数量" align="center">
          <template slot-scope="scope">{{ scope.row.sale_num }}</template>
        </el-table-column>
        <el-table-column label="成交价格" align="center">
          <template slot-scope="scope">{{ scope.row.tran_price }}</template>
        </el-table-column>
        <el-table-column label="购买商" align="center">
          <template slot-scope="scope">{{ scope.row.buyer }}</template>
        </el-table-column>
        <el-table-column label="销售人" align="center">
          <template slot-scope="scope">{{ scope.row.salesman }}</template>
        </el-table-column>
        <el-table-column label="销售时间" align="center">
          <template slot-scope="scope">{{ scope.row.sale_date }}</template>
        </el-table-column>
        <el-table-column label="销售状态" align="center">
          <template slot-scope="scope">{{ scope.row.status|orderStatusFilter  }}</template>
<!--            <el-tag type="success" v-if="scope.row.status===5">{{ scope.row.status |orderStatusFilter }}</el-tag>-->
<!--            <el-tag type="danger" v-if="scope.row.status===2">{{ scope.row.status |orderStatusFilter }}</el-tag>-->
<!--            <el-tag v-if="scope.row.status!==2 && scope.row.status!==5">{{-->
<!--                scope.row.status |orderStatusFilter-->
<!--              }}-->
<!--            </el-tag>-->
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope" v-if="scope.row.status!==5">
            <el-button size="mini" type="text" @click="handleUpdate(scope.$index, scope.row)" v-if="scope.row.status===1">编辑</el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.$index, scope.row)" v-if="scope.row.status===1">删除</el-button>
            <el-button size="mini" type="text" @click="handleGK(scope.$index, scope.row)" v-if="scope.row.status===1">改款</el-button>
            <el-button size="mini" type="text">
              <span v-if="scope.row.status <3 && scope.row.status!==2" @click="handleUpdateStatus(scope.$index, scope.row,2)">挂起</span>
              <span v-if="scope.row.status <3 &&scope.row.status===2" @click="handleUpdateStatus(scope.$index, scope.row,1)">恢复</span>
            </el-button>
            <el-button size="mini" type="text" v-if="scope.row.status===3"
                       @click="handleUpdateStatus(scope.$index, scope.row,5)">成交
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
      :title="isEdit?'编辑销售订单':'新增销售订单'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="手表名称/型号" class="marginBottom22" prop="watch_name">
          <el-select v-model="chooseMaterial" placeholder="请选择产品" class="width450" v-if="!isEdit"  @change="handleChangeMaterial()">
            <el-option
              v-for="(material) in materialList"
              :key="material.id+'edit'"
              :label="material.name"
              :value="material.name">
            </el-option>
          </el-select>
          <el-input :value="`${(mainEntry.watch_name)}/${(mainEntry.watch_type)}`" disabled class="width450"
                    v-if="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="销售数量：" class="marginBottom22" prop="sale_num">
          <el-input v-model.number="mainEntry.sale_num" class="width450" v-show="!isEdit"></el-input>
          <el-input v-model="mainEntry.sale_num" class="width450" v-show="isEdit" disabled></el-input>
        </el-form-item>
        <el-form-item label="成交价格：" class="marginBottom22" prop="tran_price">
          <el-input v-model.number="mainEntry.tran_price" class="width450" v-show="!isEdit"></el-input>
          <el-input v-model="mainEntry.tran_price" class="width450" v-show="isEdit" disabled></el-input>
        </el-form-item>
        <el-form-item label="购买商：" class="marginBottom22">
          <el-input v-model="mainEntry.buyer" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="销售人：" class="marginBottom22">
          <el-input v-model="mainEntry.salesman" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="销售日期：" class="marginBottom22" prop="sale_date">
          <el-date-picker
            v-model="mainEntry.sale_date" class="width450"
            align="right"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose('form')" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm('form')" size="small">确 定</el-button>
      </span>
    </el-dialog>
    <!--订单改款页面-->
    <el-dialog
      :title="'订单改款'"
      :visible.sync="gkDialogVisible"
      width="40%">
      <el-form :model="mainEntry"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="手表名称：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.watch_name" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="手表型号：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.watch_type" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="销售数量：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.sale_num" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="成交价格：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.tran_price" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="购买商：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.buyer" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="销售人：" style="margin-bottom: 10px">
          <el-input v-model="mainEntry.salesman" style="width: 350px" disabled></el-input>
        </el-form-item>
        <el-form-item label="销售日期：" style="margin-bottom: 10px">
          <el-date-picker style="width: 350px"
                          disabled
                          v-model="mainEntry.sale_date"
                          align="right"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="gkDialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleUpdateChangeFlag()" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listPage, save, remove, update, updateStatus} from '@/api/sale/order'
import {exportXmlSaleOrder} from '@/api/export/export'
import {listAllByType} from '@/api/warehouse_storage/material'
import {orderStatusEnums} from '@/const/saleOrderStatus'
const defaultPage = {
  page: 1,
  size: 10,
}
const defaultParam = {
  order_no: null,
  watch_name: null,
  watch_type: null,
  buyer: null,
  salesman: null,
  sale_start_date: null,
  sale_end_date: null,
  status: null,
  status_list: [1, 2, 3, 4, 5,6,7],
  change_flag: false,
}
const defaultEntry = {
  id: null,
  order_no: null,
  watch_name: null,
  watch_type: null,
  sale_num: null,
  tran_price: null,
  buyer: null,
  salesman: null,
  sale_date: null,
  status: null,
  change_flag: null,
}


export default {
  name: "OrderList",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      gkDialogVisible: false,
      isEdit: false,
      total: 0,
      orderStatusEnums,
      sale_date: null,
      materialList: [],
      chooseMaterial: null,
      rules: {
        watch_name: [
          {required: true, message: '手表名称/型号不能为空'},
        ],
        tran_price: [
          {required: true, message: '成交价格不能为空'},
          {type: 'number', message: '成交价格必须为数字值'}
        ],
        sale_num: [
          {required: true, message: '销售数量不能为空'},
          {type: 'number', message: '销售数量必须为数字值'}
        ],
        sale_date: [
          {required: true, message: '销售日期不能为空'},
        ],
      }
    }
  },
  created() {
    this.getList();
    this.getMaterialList();
  },
  filters: {
    orderStatusFilter(type) {
      if (!type) {
        return "N/A"
      }
      return orderStatusEnums.find(r => r.type === type).value;
    }
  },
  methods: {
    handleResetSearch() {
      this.page = Object.assign({}, defaultPage);
      this.param = Object.assign({}, defaultParam);
      this.sale_date = null;
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
      if (this.sale_date) {
        this.param.sale_start_date = this.sale_date[0]
        this.param.sale_end_date = this.sale_date[1]
      } else {
        this.param.sale_start_date = null;
        this.param.sale_end_date = null;
      }
      listPage(this.param, this.page).then(response => {
        this.listLoading = false;
        this.list = response.data.content;
        this.total = response.data.totalElements;
      });
    },
    handleChangeMaterial() {
      let split = this.chooseMaterial.split("/")
      this.mainEntry.watch_name = split[0];
      this.mainEntry.watch_type = split[1];
    },
    getMaterialList() {
      this.listLoading = true;
      listAllByType({materialType: 3, childrenType: 4}).then(response => {
        this.listLoading = false;
        let temp = response.data;
        temp.forEach(t => {
          this.materialList.push({id: t.id, name: t.material_name + "/" + t.material_model})
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
    //改款（唤起dialog）
    handleGK(index, row) {
      this.gkDialogVisible = true;
      this.isEdit = true;
      this.mainEntry = Object.assign({}, row);
    },
    handleClose(formName) {
      this.$refs[formName].resetFields();
      this.dialogVisible = false;
      this.chooseMaterial = null;
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
      this.$confirm('是否要删除该销售单?', '提示', {
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
    //更改订单状态
    handleUpdateStatus(index, row, status) {
      this.$confirm('是否要更改订单状态?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.status = status
        updateStatus(row).then(response => {
          this.$message({
            type: 'success',
            message: '更改状态成功!'
          });
          this.getList();
        });
      });
    },
    handleUpdateChangeFlag(index, row, status) {
      this.$confirm('是否要更改订单数量或成交价格?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.mainEntry.change_flag = true;
        update(this.mainEntry).then(response => {
          this.$message({
            message: '修改成功！',
            type: 'success'
          });
          this.gkDialogVisible = false;
          this.getList();
        })
      });
    },
    handleExport() {
      this.$confirm('是否要导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        exportXmlSaleOrder(this.param).then(res => {
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
