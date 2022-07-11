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
        <el-form :inline="true" :model="param" size="small" label-width="140px">
          <el-form-item>
            <el-input v-model="param.account" class="input-width" placeholder="帐号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.name" class="input-width" placeholder="姓名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="param.job_number" class="input-width" placeholder="工号" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.department" clearable placeholder="请选择部门" @clear="param.department=null">
              <el-option
                v-for="department in departments"
                :key="department.id"
                :value="department.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.delete_flag" clearable placeholder="请选择账号状态" @clear="param.delete_flag=null">
              <el-option
                v-for="flag in enableFlags"
                :label="flag.name"
                :key="flag.code"
                :value="flag.code">
              </el-option>
            </el-select>
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
                v-loading="listLoading">
        <el-table-column label="帐号" align="center">
          <template slot-scope="scope">{{ scope.row.account }}</template>
        </el-table-column>
        <el-table-column label="姓名" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="工号" align="center">
          <template slot-scope="scope">{{ scope.row.job_number }}</template>
        </el-table-column>
        <el-table-column label="联系电话" align="center">
          <template slot-scope="scope">{{ scope.row.phone }}</template>
        </el-table-column>
        <el-table-column label="性别" align="center">
          <template slot-scope="scope">{{ scope.row.sex | sexFilter }}</template>
        </el-table-column>
        <el-table-column label="年龄" align="center">
          <template slot-scope="scope">{{ scope.row.age }}</template>
        </el-table-column>
        <el-table-column label="生日" align="center">
          <template slot-scope="scope">{{ scope.row.birthday }}</template>
        </el-table-column>
        <el-table-column label="入职日期" align="center">
          <template slot-scope="scope">{{ scope.row.entry_date }}</template>
        </el-table-column>
        <el-table-column label="部门" align="center">
          <template slot-scope="scope">{{ scope.row.department }}</template>
        </el-table-column>
        <el-table-column label="职务" align="center">
          <template slot-scope="scope">{{ scope.row.duty }}</template>
        </el-table-column>
        <el-table-column label="添加时间" width="160" align="center">
          <template slot-scope="scope">{{ scope.row.createTime | formatDateTime }}</template>
        </el-table-column>
        <el-table-column label="是否启用" width="140" align="center">
          <template slot-scope="scope">
            <el-switch
              @change="handleStatusChange(scope.$index, scope.row)"
              :active-value="0"
              :inactive-value="1"
              v-model="scope.row.delete_flag">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleSelectRole(scope.$index, scope.row)">分配角色
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
      :title="isEdit?'编辑用户':'添加用户'"
      :visible.sync="dialogVisible"
      width="40%">
      <el-form :model="account"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="帐号：" style="margin-bottom: 10px">
          <el-input v-model="account.account" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="密码：" style="margin-bottom: 10px">
          <el-input v-model="account.password" type="password" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="姓名：" style="margin-bottom: 10px">
          <el-input v-model="account.name" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="工号：" style="margin-bottom: 10px">
          <el-input v-model="account.job_number" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="联系电话：" style="margin-bottom: 10px">
          <el-input v-model="account.phone" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="性别：" style="margin-bottom: 10px">
          <el-radio-group v-model="account.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
            <el-radio :label="-1">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄：" style="margin-bottom: 10px">
          <el-input v-model="account.age" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="生日：" style="margin-bottom: 10px">
          <el-date-picker
            v-model="account.birthday"
            align="right"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="入职日期：" style="margin-bottom: 10px">
          <el-date-picker
            v-model="account.entry_date"
            align="right"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="离职日期：" style="margin-bottom: 10px">
          <el-date-picker
            v-model="account.quit_date"
            align="right"
            type="date"
            placeholder="选择日期"
            value-format="yyyy-MM-dd"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="部门：" style="margin-bottom: 10px">
          <el-select v-model="account.department" clearable placeholder="请选择">
            <el-option
              v-for="department in departments"
              :key="department.id"
              :value="department.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职务：" style="margin-bottom: 10px">
          <el-input v-model="account.duty" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="备注：" style="margin-bottom: 10px">
          <el-input v-model="account.remarks"
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="是否启用：">
          <el-radio-group v-model="account.delete_flag">
            <el-radio :label="0">是</el-radio>
            <el-radio :label="1">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm()" size="small">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="分配角色"
      :visible.sync="allocDialogVisible"
      width="30%">
      <el-select v-model="allocRoleIds" multiple placeholder="请选择" size="small" style="width: 80%">
        <el-option
          v-for="item in allRoleList"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      <span slot="footer" class="dialog-footer">
        <el-button @click="allocDialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleAllocDialogConfirm()" size="small">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>
<script>
import {fetchList, createAccount, updateAccount, updateDeleteFlag, deleteAccount, getRoleByAccount, allocRole} from '@/api/backstage/account';
import {listAll as listDepartment} from '@/api/backstage/department';
import {listAll as listRole} from '@/api/backstage/role';

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
};
const enableFlags = [
  {
  code: 0,
  name: "启用"
  },
  {
  code: 1,
  name: "禁用"
  }
]
const defaultParam = {
  account: null,
  name: null,
  job_number: null,
  department: null,
  delete_flag: null
}
const defaultAccount = {
  id:null,
  account: null,
  password: null,
  name: null,
  phone:null,
  job_number: null,
  sex: null,
  age: null,
  birthday: null,
  entry_date: null,
  quit_date: null,
  department: null,
  duty: null,
  remarks: null,
  delete_flag: 0
};
export default {
  name: 'adminList',
  data() {
    return {
      page: Object.assign({}, defaultPage),
      account: Object.assign({}, defaultAccount),
      param: Object.assign({}, defaultParam),
      departments: null,
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      allocDialogVisible: false,
      allocRoleIds: [],
      allRoleList: [],
      allocAccountId: null,
      enableFlags,
      // 日期快捷选择
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      }
    }
  },
  created() {
    this.getList();
    this.getDepartmentList()
    this.getRoleList();
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
    // 添加（唤起dialog）
    handleAdd() {
      this.dialogVisible = true;
      this.isEdit = false;
      this.account = Object.assign({}, defaultAccount);
    },
    //禁用
    handleStatusChange(index, row) {
      this.$confirm('是否要修改该状态?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateDeleteFlag({id:row.id, delete_flag:row.delete_flag}).then(response => {
          this.$message({
            type: 'success',
            message: '修改成功!'
          });
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消修改'
        });
        this.getList();
      });
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该用户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAccount(row.id).then(response => {
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
      this.account = Object.assign({}, row);
    },
    //保存或更新信息
    handleDialogConfirm() {
      this.$confirm('是否确认保存?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.isEdit) {
          updateAccount(this.account).then(response => {
            this.$message({
              message: '修改成功！',
              type: 'success'
            });
            this.dialogVisible = false;
            this.getList();
          })
        } else {
          createAccount(this.account).then(response => {
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
    //分配角色
    handleAllocDialogConfirm() {
      this.$confirm('是否要确认?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = new URLSearchParams();
        params.append("roleIds", this.allocRoleIds);
        allocRole(this.allocAccountId,params).then(response => {
          this.$message({
            message: '分配成功！',
            type: 'success'
          });
          this.allocDialogVisible = false;
        })
      })
    },
    getRoleList(){
      this.listLoading = true;
      listRole().then(response=>{
        this.listLoading = false
        this.allRoleList = response.data
      })
    },
    handleSelectRole(index, row) {
      this.allocAccountId = row.id;
      this.allocDialogVisible = true;
      this.listLoading = true;
      getRoleByAccount(this.allocAccountId).then(response=>{
        this.listLoading = false
        this.allocRoleIds = response.data.map(role=>role.id)
      })
    },
    //查询
    getList() {
      this.listLoading = true;
      fetchList(this.page, this.param).then(response => {
        this.listLoading = false;
        this.list = response.data.content;
        this.page.total = response.data.totalElements;
      });
    },
    getDepartmentList() {
      this.listLoading = true;
      listDepartment().then(response => {
        this.listLoading = false;
        this.departments = response.data
      });
    },
    getRoleListByAdmin(adminId) {
      getRoleByAccount(adminId).then(response => {
        let allocRoleList = response.data;
        this.allocRoleIds = [];
        if (allocRoleList != null && allocRoleList.length > 0) {
          for (let i = 0; i < allocRoleList.length; i++) {
            this.allocRoleIds.push(allocRoleList[i].id);
          }
        }
      });
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>


