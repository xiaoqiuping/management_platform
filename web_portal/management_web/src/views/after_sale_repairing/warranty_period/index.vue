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
            <el-input v-model="param.buyer_name" class="input-width" placeholder="客户姓名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.watch_name" class="input-width" placeholder="手表名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.watch_type" class="input-width" placeholder="手表型号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.status" placeholder="请选择状态" clearable @clear="param.status = null">
              <el-option
                v-for="status in statusEnum"
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
        <el-table-column label="维修单号" align="center" width="220px">
          <template slot-scope="scope">{{ scope.row.order_no }}</template>
        </el-table-column>
        <el-table-column label="手表名称" align="center">
          <template slot-scope="scope">{{ scope.row.watch_name }}</template>
        </el-table-column>
        <el-table-column label="手表型号" align="center">
          <template slot-scope="scope">{{ scope.row.watch_type }}</template>
        </el-table-column>
        <el-table-column label="客户名称" align="center">
          <template slot-scope="scope">{{ scope.row.buyer_name }}</template>
        </el-table-column>
        <el-table-column label="维修金额" align="center">
          <template slot-scope="scope">{{ scope.row.repair_amount }}</template>
        </el-table-column>
        <el-table-column label="维修人" align="center">
          <template slot-scope="scope">{{ scope.row.repair_man }}</template>
        </el-table-column>
        <el-table-column label="送修日期" align="center">
          <template slot-scope="scope">{{ scope.row.repair_date }}</template>
        </el-table-column>
        <el-table-column label="送回日期" align="center">
          <template slot-scope="scope">{{ scope.row.send_back_date }}</template>
        </el-table-column>
        <el-table-column label="订单状态" align="center">
          <template slot-scope="scope">{{ scope.row.status |statusFilter }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleDrawer(scope.$index, scope.row)">
              详情
            </el-button>
            <el-button size="mini" v-if="scope.row.status===1"
                       type="text"
                       @click="handleUpdate(scope.$index, scope.row)">
              编辑
            </el-button>
            <el-button size="mini"
                       type="text"
                       @click="handleFinished(scope.$index, scope.row)" v-if="scope.row.status==3">完成
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
      :title="isEdit?'编辑维修单':'新增维修单'"
      :visible.sync="dialogVisible"
      @close="handleClose('form')"
      width="40%">
      <el-form :model="mainEntry"
               ref="form" :rules="rules"
               label-width="150px" size="small">
        <el-form-item label="手表名称：" class="marginBottom22" prop="watch_name">
          <el-input v-model="mainEntry.watch_name" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="手表型号：" class="marginBottom22" prop="watch_type">
          <el-input v-model="mainEntry.watch_type" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="购买日期：" class="marginBottom22">
          <el-date-picker class="width450"
                          v-model="mainEntry.buy_date"
                          align="right"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="保修截止日期：" class="marginBottom22">
          <el-date-picker class="width450"
                          v-model="mainEntry.guarantee_date"
                          align="right"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="客户名称：" class="marginBottom22" prop="buyer_name">
          <el-input v-model="mainEntry.buyer_name" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="客户联系电话：" class="marginBottom22">
          <el-input v-model="mainEntry.buyer_phone" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="客户地址：" class="marginBottom22">
          <el-input v-model="mainEntry.buyer_address"
                    type="textarea"
                    maxlength="100"
                    :rows="2"
                    show-word-limit
                    class="width450"></el-input>
        </el-form-item>
        <el-form-item label="故障描述：" class="marginBottom22" prop="fault_description">
          <el-input v-model="mainEntry.fault_description"
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    class="width450"></el-input>
        </el-form-item>
        <el-form-item label="维修金额：" class="marginBottom22" prop="repair_amount">
          <el-input v-model.number="mainEntry.repair_amount" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="维修人：" class="marginBottom22">
          <el-input v-model="mainEntry.repair_man" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="送修日期：" class="marginBottom22">
          <el-date-picker class="width450"
                          v-model="mainEntry.repair_date"
                          align="right"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="维修分析：" class="marginBottom22">
          <el-input v-model="mainEntry.repair_analysis"
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    class="width450"></el-input>
        </el-form-item>
        <el-form-item label="送回日期：" class="marginBottom22">
          <el-date-picker class="width450"
                          v-model="mainEntry.send_back_date"
                          align="right"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择日期">
          </el-date-picker>
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
          <span>维修单号：{{ mainEntry.order_no }}</span>
        </div>
        <div>
          <el-form :model="mainEntry"
                   label-width="150px" size="small">
            <el-form-item label="维修类别：" style="margin-bottom: 10px">
              <span class="margintop10">保内维修</span>
            </el-form-item>
            <el-form-item label="手表名称：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.watch_name }}</span>
            </el-form-item>
            <el-form-item label="手表型号：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.watch_type }}</span>
            </el-form-item>
            <el-form-item label="购买日期：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.buy_date }}</span>
            </el-form-item>
            <el-form-item label="保修截止日期：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.guarantee_date }}</span>
            </el-form-item>
            <el-form-item label="客户名称：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.buyer_name }}</span>
            </el-form-item>
            <el-form-item label="客户联系电话：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.buyer_phone }}</span>
            </el-form-item>
            <el-form-item label="客户联系地址：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.buyer_address }}</span>
            </el-form-item>
            <el-form-item label="故障描述：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.fault_description }}</span>
            </el-form-item>
            <el-form-item label="维修金额：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.repair_amount }}</span>
            </el-form-item>
            <el-form-item label="维修人：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.repair_man }}</span>
            </el-form-item>
            <el-form-item label="送修日期：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.repair_date }}</span>
            </el-form-item>
            <el-form-item label="维修分析：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.repair_analysis }}</span>
            </el-form-item>
            <el-form-item label="送回日期：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.send_back_date }}</span>
            </el-form-item>
            <el-form-item label="备注：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.remarks }}</span>
            </el-form-item>
            <el-form-item label="订单状态：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.status  | statusFilter }}</span>
            </el-form-item>
            <el-form-item label="质检人：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.examine_man }}</span>
            </el-form-item>
            <el-form-item label="质检备注：" style="margin-bottom: 10px">
              <span class="margintop10">{{ mainEntry.examine_remarks }}</span>
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </el-drawer>
  </div>
</template>


<script>
import {listPage, save, remove, update, updateStatus} from '@/api/after_sale_repair/repairOrder'
import {exportXmlRepairOrder} from '@/api/export/export'

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const defaultParam = {
  type: 1,
  status: null,
  buyer_name: null,
  watch_name: null,
  watch_type: null,
}
const defaultEntry = {
  id: null,
  order_no: null,
  watch_name: null,
  watch_type: null,
  buy_date: null,
  guarantee_date: null,
  buyer_name: null,
  buyer_phone: null,
  buyer_address: null,
  fault_description: null,
  repair_amount: null,
  repair_man: null,
  repair_date: null,
  repair_analysis: null,
  send_back_date: null,
  remarks: null,
  type: 1,
  status: null,
  examine_man: null,
  examine_remarks: null,
};

const statusEnum = [
  {
    type: 1,
    value: "新建",
  },
  {
    type: 2,
    value: "质检未通过",
  },
  {
    type: 3,
    value: "质检通过",
  },
  {
    type: 4,
    value: "维修完成",
  },
]
/*保内维修*/
export default {
  name: "WarrantyPeriod",
  data() {
    return {
      page: Object.assign({}, defaultPage),
      param: Object.assign({}, defaultParam),
      mainEntry: Object.assign({}, defaultEntry),
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      drawerVisible: false,
      statusEnum,
      rules: {
        watch_name: [
          {required: true, message: '手表名称不能为空'},
        ],
        watch_type: [
          {required: true, message: '手表型号不能为空'},
        ],
        repair_amount: [
          {required: true, message: '维修金额不能为空'},
          {type: 'number', message: '维修金额必须为数字值'}
        ],
        buyer_name: [
          {required: true, message: '客户名称不能为空'},
        ],
        fault_description: [
          {required: true, message: '故障描述不能为空'},
        ],
      }
    }
  },
  created() {
    this.getList();
  },
  filters: {
    statusFilter(status) {
      if (!status) {
        return "N/A"
      }
      return statusEnum.find(s => s.type === status).value;
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
    handleReset() {
      this.getList();
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
    //唤起抽屉
    handleDrawer(index, row) {
      this.drawerVisible = true;
      this.mainEntry = Object.assign({}, row);
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该维修单?', '提示', {
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
    //维修完成
    handleFinished(index, row) {
      this.$confirm('是否要更改状态信息?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateStatus(row.id, 4).then(response => {
          this.$message({
            type: 'success',
            message: '更改成功!'
          });
          this.getList();
        });
      });
    },
    handleExport() {
      this.$confirm('是否要导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        exportXmlRepairOrder(this.param).then(res => {
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
