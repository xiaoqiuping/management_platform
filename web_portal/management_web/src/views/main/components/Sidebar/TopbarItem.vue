<template>
  <div>
    <el-menu
      :default-active="''+top_menu_index"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
      background-color="#304156"
      text-color="#fff"
      active-text-color="#ffd04b">
      <div class="logo_info" style="z-index: 2000">
        <div style="font-size: 16px">景时表业</div>
      </div>
      <el-menu-item :index="index+''" :key="menu.code" v-for="(menu,index) in top_menus"><span
        class="padding15">{{ menu.name }}</span></el-menu-item>
      <div class="loginout" onclick="">
        <el-dropdown trigger="click">
          <span class="el-dropdown-link">
              {{ name }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item><span @click="dialogVisible = true" style="display:block;">修改密码</span></el-dropdown-item>
            <el-dropdown-item><span @click="logout" style="display:block;">退出</span></el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-menu>
    <el-dialog
      :title="'修改密码'"
      :visible.sync="dialogVisible"
      :append-to-body="true"
      @close="handleClose()"
      width="40%">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm"
               label-width="150px" size="small">
        <el-form-item label="新密码：" class="marginBottom22" prop="pass">
          <el-input type="password"  v-model="ruleForm.pass" class="width450"></el-input>
        </el-form-item>
        <el-form-item label="再次确认：" class="marginBottom22" prop="checkPass">
          <el-input type="password"  v-model="ruleForm.checkPass" class="width450"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose()" size="small">取 消</el-button>
        <el-button type="primary" @click="changePassword('ruleForm')" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import {getCookie} from '@/utils/support'
import store from '@/store'
import router from '@/router'
import {updatePassword} from '@/api/backstage/account';


const defaultAccount = {
  account: null,
  password: null,
}


export default {
  name: 'TopbarItem',
  props: {
    top_menus: {
      type: Array
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'top_menu_index',
    ])
  },
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      dialogVisible: false,
      account: Object.assign({}, defaultAccount),
      flag:true,
      ruleForm: {
        pass: '',
        checkPass: '',
      },
      rules: {
        pass: [
          {validator: validatePass, trigger: 'blur'}
        ],
        checkPass: [
          {validator: validatePass2, trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    handleSelect(key) {
      let account = getCookie('username');
      let menuId = this.top_menus[key].id
      this.$store.dispatch('GetSiderMenus', {account, menuId}).then(res => {
        this.$store.dispatch('GenerateRoutes', {menus: res, account}).then(() => { // 生成可访问的路由表
          router.addRoutes(store.getters.addRouters); // 动态添加可访问路由表
        })
      })
    },
    changePassword(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.account.account=getCookie('username');
          this.account.password = this.ruleForm.pass;
          this.$confirm('是否要修改密码?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            updatePassword(this.account).then(response => {
              this.$message({
                type: 'success',
                message: '修改成功!'
              });
              this.dialogVisible = false;
              this.$store.dispatch('LogOut').then(() => {
                location.reload() // 为了重新实例化vue-router对象 避免bug
              })
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '取消修改'
            });
          });
        } else {
          return false;
        }
      });
    },
    handleClose(){
      this.dialogVisible = false
      this.ruleForm= {
        pass: null,
        checkPass: null,
      }
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    }
  }
}
</script>
<style scoped>
.padding15 {
  padding: 0 15px;
}

.logo_info {
  float: left;
  height: 60px;
  width: 180px !important;
  line-height: 60px;
  border-right: 1px solid #d3dce6;
  border-bottom: 1px solid #d3dce6;
  color: white;
  box-sizing: border-box;
  margin: 0;
  text-align: center;
}

.loginout {
  float: right;
  height: 60px;
  line-height: 60px;
  color: white;
  box-sizing: border-box;
  margin-right: 15px;
  text-align: center;
}

.el-dropdown-link {
  cursor: pointer;
  color: white;
}

.el-icon-arrow-down {
  font-size: 12px;
}
</style>
