<template>
  <el-card class="form-container" shadow="never" style="padding: 10px">
    <div v-for="(menu,index) in allMenu" :class="index===0?'top-line':null" :key="'menu'+menu.id">
      <el-row class="table-layout" style="background: #F2F6FC;">
        <el-checkbox v-model="menu.checked"
                     :indeterminate="isIndeterminate(menu)"
                     @change="handleCheckAllChange(menu)">
          {{ menu.name }}
        </el-checkbox>
      </el-row>
      <div v-for="secondMenu in menu.childs" :key="secondMenu.id">
        <el-row class="table-layout" style="padding: 5px 20px">
          <el-col :span="8" style="padding: 4px 0">
            <el-checkbox v-model="secondMenu.checked" @change="handleCheckAllChange(secondMenu)"
                         :indeterminate="isIndeterminate(secondMenu)">
              {{ secondMenu.name }}
            </el-checkbox>
          </el-col>
        </el-row>
        <el-row class="table-layout">
          <el-col :span="8" v-for="threeChild in secondMenu.childs" :key="threeChild.id" style="padding: 4px 0">
            <el-checkbox v-model="threeChild.checked" @change="handleCheckChange(threeChild)">
              {{ threeChild.name }}
            </el-checkbox>
          </el-col>
        </el-row>
      </div>
    </div>
    <div style="margin-top: 20px" align="center">
      <el-button type="primary" @click="handleSave()">保存</el-button>
      <el-button @click="handleClear()">清空</el-button>
    </div>

  </el-card>
</template>

<script>
import {listAll} from '@/api/backstage/menu';
import {allocMenu} from '@/api/backstage/role';

export default {
  name: "allocMenu",
  data() {
    return {
      roleId: null,
      allMenu: null,
    };
  },
  created() {
    this.roleId = this.$route.query.roleId;
    this.getAllMenuList();
  },
  methods: {
    getAllMenuList() {
      listAll(this.roleId).then(response => {
        this.allMenu = response.data;
      });
    },
    //是否子项目有选项
    isIndeterminate(menu) {
      if (menu.level === 0) {
        let count = 0;
        let sum = 0;
        menu.childs.forEach(c => {
          sum += c.childs.length;
          c.childs.forEach(cc => {
            if (cc.checked === true) {
              count++;
            }
          })
        })
        return count > 0 && count < sum
      } else {
        let count = 0;
        menu.childs.forEach(c => {
          if (c.checked === true) {
            count++;
          }
        })
        return count > 0 && count < menu.childs.length
      }
    },
    //针对3级目录点击时
    isAllChecked(menu) {
      let checkedCount = 0;
      for (let i = 0; i < menu.childs.length; i++) {
        if (menu.childs[i].checked === true) {
          checkedCount++;
        }
      }
      if (checkedCount === 0) {
        return false;
      }
      return checkedCount === menu.childs.length;
    },
    getParentMenu(menu) {
      if (menu.level === 0) {
        return menu;
      }
      if (menu.level === 1) {
        return this.allMenu.filter(m => m.id === menu.parent_id)[0];
      }
      if (menu.level === 2) {
        let parentId = menu.parent_id;
        for (let i = 0; i < this.allMenu.length; i++) {
          let childs = this.allMenu[i].childs
          if (childs && childs.length > 0) {
            let filterElement = childs.filter(c => c.id === parentId)[0]
            if (filterElement) {
              return filterElement;
            }
          }
        }
      }
    },
    handleSave() {
      this.$confirm('是否分配菜单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let checkedMenu = new Set();
        if (this.allMenu != null && this.allMenu.length > 0) {
          this.allMenu.forEach(parent => {
            let flag1 = false;
            if (parent.checked) {
              checkedMenu.add(parent.id);
            }
            let secondChilds = parent.childs
            if (secondChilds != null && secondChilds.length > 0) {
              secondChilds.forEach(secondChild => {
                if (secondChild.checked) {
                  checkedMenu.add(secondChild.id);
                  flag1 = true;
                }
                let flag2 = false;
                let thirdChilds = secondChild.childs
                if (thirdChilds != null && thirdChilds.length > 0) {
                  thirdChilds.forEach(thirdChild => {
                    if (thirdChild.checked) {
                      checkedMenu.add(thirdChild.id);
                      flag2 = true;
                    }
                  });
                }
                if (flag2) {
                  checkedMenu.add(secondChild.id);
                  flag1 = true;
                }
              });
            }
            if (flag1) {
              checkedMenu.add(parent.id);
            }
          });
        }
        console.log(checkedMenu)
        let params = new URLSearchParams();
        params.append("menuIds", Array.from(checkedMenu));
        allocMenu(this.roleId,params).then(response => {
          this.$message({
            message: '分配成功',
            type: 'success',
            duration: 1000
          });
          this.$router.back();
        })
      })
    },
    //全部清空
    handleClear() {
      this.allMenu.forEach(item => {
        item.checked = false;
        item.childs.forEach(sec => {
          sec.checked = false;
          sec.childs.forEach(thr => {
            thr.checked = false;
          })
        })
      });
      this.$forceUpdate();
    },
    //全选
    handleCheckAllChange(menu) {
      if (menu.level === 0) {
        menu.childs.forEach(c => {
          c.checked = menu.checked;
          let childs = c.childs;
          for (let i = 0; i < childs.length; i++) {
            c.childs[i].checked = menu.checked;
          }
        })
      } else if (menu.level === 1) {
        let childs = menu.childs;
        for (let i = 0; i < childs.length; i++) {
          menu.childs[i].checked = menu.checked;
        }
        let parentMenu = this.getParentMenu(menu);
        parentMenu.checked = this.isAllChecked(parentMenu);
      }
      this.$forceUpdate();
    },
    //单选
    handleCheckChange(threeChild) {
      let secondMenu = this.getParentMenu(threeChild);
      secondMenu.checked = this.isAllChecked(secondMenu);
      let parentMenu = this.getParentMenu(secondMenu)
      if(!secondMenu.checked){
        parentMenu.checked = secondMenu.checked
      }
      if (secondMenu.checked) {
        let parentMenu1 = this.getParentMenu(secondMenu);
        parentMenu1.checked = this.isAllChecked(parentMenu1);
      }
      this.$forceUpdate();
    }
  }
}
</script>

<style scoped>
.table-layout {
  padding: 20px;
  border-left: 1px solid #DCDFE6;
  border-right: 1px solid #DCDFE6;
  border-bottom: 1px solid #DCDFE6;
}

.top-line {
  border-top: 1px solid #DCDFE6;
}

</style>
