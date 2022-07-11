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
            <el-input v-model="param.name" class="input-width" placeholder="角色名称" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.department_name" clearable placeholder="请选择部门" @clear="param.department_name=null">
              <el-option
                v-for="department in departments"
                :key="department.id"
                :value="department.name">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.enable_flag" clearable placeholder="请选择角色状态" @clear="param.enable_flag=null">
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
                v-loading="listLoading" >
        <el-table-column label="角色名称" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="部门名称" align="center">
          <template slot-scope="scope">{{ scope.row.department_name }}</template>
        </el-table-column>
        <el-table-column label="备注" align="center">
          <template slot-scope="scope">{{ scope.row.remarks }}</template>
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
              v-model="scope.row.enable_flag">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleAllocMenu(scope.$index, scope.row)">分配菜单
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
      <el-form :model="role"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="角色名称：" style="margin-bottom: 10px">
          <el-input v-model="role.name" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="部门：" style="margin-bottom: 10px">
          <el-select v-model="role.department_name" clearable placeholder="请选择" style="width: 350px">
            <el-option
              v-for="department in departments"
              :key="department.id"
              :value="department.name">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注：" style="margin-bottom: 10px">
          <el-input v-model="role.remarks"
                    type="textarea"
                    maxlength="300"
                    :rows="5"
                    show-word-limit
                    style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="是否启用：">
          <el-radio-group v-model="role.enable_flag">
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

  </div>

</template>
<script>
import {listPage, save, update, updateEnableFlag, remove} from '@/api/backstage/role';
import {listAll} from '@/api/backstage/department';

const defaultPage = {
  page: 1,
  size: 10,
  total: 0
}
const enableFlags = [{
  code: 0,
  name: "启用"
}, {
  code: 1,
  name: "禁用"
}
]

const defaultParam = {
  name: null,
  department_name: null,
  enable_flag: null
}

const defaultRole = {
  id: null,
  name: null,
  department_name: null,
  remarks: null,
  enable_flag: 0
};


export default {
  name: 'RoleList',
  data() {
    return {
      page: Object.assign({}, defaultPage),
      role: Object.assign({}, defaultRole),
      param: Object.assign({}, defaultParam),
      departments: null,
      enableFlags,
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      allocDialogVisible: false,
    }
  },
  created() {
    this.getList()
    this.getDepartmentList()
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
    getDepartmentList() {
      this.listLoading = true;
      listAll().then(response => {
        this.listLoading = false;
        this.departments = response.data
      });
    },
    // 添加（唤起dialog）
    handleAdd() {
      this.dialogVisible = true;
      this.isEdit = false;
      this.role = Object.assign({}, defaultRole);
    },
    //禁用
    handleStatusChange(index, row) {
      this.$confirm('是否要修改该状态?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateEnableFlag({id: row.id, enable_flag: row.enable_flag}).then(response => {
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
      this.role = Object.assign({}, row);
    },
    //保存或更新信息
    handleDialogConfirm() {
      this.$confirm('是否确认保存?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.isEdit) {
          update(this.role).then(response => {
            this.$message({
              message: '修改成功！',
              type: 'success'
            });
            this.dialogVisible = false;
            this.getList();
          })
        } else {
          save(this.role).then(response => {
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

    handleAllocMenu(index,row){
      this.$router.push({path:'/backstage/allocMenu',query:{roleId:row.id}})
    },
  }
}

</script>
<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>


