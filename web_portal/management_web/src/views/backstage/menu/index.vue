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
            <el-input v-model="param.name" class="input-width" placeholder="菜单名称名称" clearable @clear="param.name=null"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="param.level" clearable placeholder="请选择菜单级数" @clear="param.level=null">
              <el-option
                v-for="lev in levels"
                :key="lev.id"
                :label="lev.name"
                :value="lev.id">
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
        <el-table-column label="菜单名称" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="菜单代码" align="center">
          <template slot-scope="scope">{{ scope.row.code }}</template>
        </el-table-column>
        <el-table-column label="菜单级数" align="center">
          <template slot-scope="scope">{{ scope.row.level +1 }}</template>
        </el-table-column>
<!--        <el-table-column label="前端图标" align="center">-->
<!--          <template slot-scope="scope">{{ scope.row.icon }}</template>-->
<!--        </el-table-column>-->
        <el-table-column label="是否显示" width="140" align="center">
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
      :title="isEdit?'编辑菜单':'添加用菜单'"
      :visible.sync="dialogVisible"
      width="40%">
      <el-form :model="menu"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="菜单名称：" style="margin-bottom: 10px">
          <el-input v-model="menu.name" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="菜单代码：" style="margin-bottom: 10px">
          <el-input v-model="menu.code" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="菜单级数：" style="margin-bottom: 10px">
          <el-select v-model="menu.level" clearable placeholder="请选择" style="width: 350px" @change="clearParentMenus">
            <el-option
              v-for="lev in levels"
              :key="lev.id"
              :label="lev.name"
              :value="lev.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="父级菜单：" style="margin-bottom: 10px" v-if="menu.level !==0">
          <el-select v-model="menu.parent_id" clearable placeholder="请选择" style="width: 350px" @focus="getParentMenus()">
            <el-option
              v-for="lev in parentMenus"
              :key="lev.id"
              :label="lev.name"
              :value="lev.id">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="图标：" style="margin-bottom: 10px" v-if="this.menu.level!==0">
          <el-input v-model="menu.icon" style="width: 350px"></el-input>
        </el-form-item>
        <el-form-item label="是否显示：">
          <el-radio-group v-model="menu.enable_flag">
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
import {listPage, save, remove, update, listByLevel, updateEnableFlag} from '@/api/backstage/menu'

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
}]
const levels = [{
  id: 0,
  name: "一级菜单"
}, {
  id: 1,
  name: "二级菜单"
}, {
  id: 2,
  name: "三级菜单"
}
]
const defaultParam = {
  name: null,
  level: null,
  enable_flag: null
}
const defaultMenu = {
  id: null,
  name: null,
  level: null,
  icon: null,
  enable_flag: 0,
  parent_id: null,
  sort_num: null
};


export default {
  name: 'MenuList',
  data() {
    return {
      page: Object.assign({}, defaultPage),
      menu: Object.assign({}, defaultMenu),
      param: Object.assign({}, defaultParam),
      levels,
      enableFlags,
      parentMenus:null,
      list: null,
      listLoading: false,
      dialogVisible: false,
      isEdit: false,
      allocDialogVisible: false,
    }
  },
  created() {
    this.getList()
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
    getParentMenus(){
      if(!this.menu.level){
        this.$message({
          type: 'warning',
          message: '请选择菜单级数!'
        });
        return ;
      }
      this.listLoading = true;
      listByLevel(this.menu.level-1).then(response => {
        this.listLoading = false;
        this.parentMenus = response.data;
      });
    },
    clearParentMenus(){
      this.parentMenus =null
      this.menu.parent_id = null
    },
    // 添加（唤起dialog）
    handleAdd() {
      this.dialogVisible = true;
      this.isEdit = false;
      this.menu = Object.assign({}, defaultMenu);
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
      });
    },
    //删除
    handleDelete(index, row) {
      this.$confirm('是否要删除该菜单?', '提示', {
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
      this.menu = Object.assign({}, row);
    },
    //保存或更新信息
    handleDialogConfirm() {
      this.$confirm('是否确认保存?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.isEdit) {
          update(this.menu).then(response => {
            this.$message({
              message: '修改成功！',
              type: 'success'
            });
            this.dialogVisible = false;
            this.getList();
          })
        } else {
          save(this.menu).then(response => {
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
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
@import "src/styles/common.scss";
</style>
