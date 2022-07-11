import {login, logout, getUserDetail, getSiderMenus} from '@/api/backstage/login'
import {getToken, setToken, removeToken} from '@/utils/auth'
import {getCookie} from "@/utils/support"
import store from "../index"
import router from "../../router"

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    sider_menus: [],
    top_menus: [],
    top_menu_index: 0,
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_TOP_MENUS: (state, top_menus) => {
      state.top_menus = top_menus
    },
    SET_SIDER_MENUS: (state, sider_menus) => {
      state.sider_menus = sider_menus
    },
    SET_TOP_MENU_INDEX: (state, top_menu_index) => {
      state.top_menu_index = top_menu_index
    },
  },

  actions: {
    // 登录
    Login({commit}, userInfo) {
      const username = userInfo.username.trim()
      return new Promise((resolve, reject) => {
        login(username, userInfo.password).then(response => {
          setToken(response.data)
          commit('SET_TOKEN', response.data)
          commit('SET_NAME', userInfo.username)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({commit, dispatch}, data) {
      let topMenu = data.topMenu
      return new Promise((resolve, reject) => {
        let account = getCookie('username')
        getUserDetail(account).then(response => {
          //解决浏览器刷新的时候一些必要参数问题
          commit('SET_TOP_MENUS', response.data.top_menus)
          commit('SET_NAME', account)
          let menuId;
          if (!topMenu) {
            commit('SET_TOP_MENU_INDEX', 0)
            menuId = response.data.top_menus[0].id;
          } else {
            for (let i = 0; i < response.data.top_menus.length; i++) {
              if (response.data.top_menus[i].code === topMenu) {
                commit('SET_TOP_MENU_INDEX', i)
                menuId = response.data.top_menus[i].id;
              }
            }
          }
          resolve({account, menuId: menuId})
        }).catch(error => {
          reject(error)
        })
      })
    },

    GetSiderMenus({commit}, data) {
      return new Promise((resolve, reject) => {
        getSiderMenus(data.account, data.menuId).then(response => {
          let siderMenus = response.data
          resolve(siderMenus)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({commit, state}) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_TOP_MENUS', '')
          commit('SET_SIDER_MENUS', '')
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({commit}) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
