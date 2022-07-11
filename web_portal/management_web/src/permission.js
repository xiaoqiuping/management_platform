import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import {Message} from 'element-ui'
import {getToken} from '@/utils/auth' // 验权

const whiteList = ['/login'] // 不重定向白名单
router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    if (to.path === '/login') {
      next({path: '/'})
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {
      next()
      if (store.getters.top_menus.length === 0) {
        let path = to.path
        let topMenu = path.split("/")[1]
        store.dispatch('GetInfo',{topMenu}).then(res => { // 拉取用户信息
          let menuId = res.menuId;
          let account = res.account;
          store.dispatch('GetSiderMenus', {account, menuId}).then(res => {
            store.dispatch('GenerateRoutes', {menus: res, account}).then(() => { // 生成可访问的路由表
              router.addRoutes(store.getters.addRouters); // 动态添加可访问路由表
              next({...to, replace: true})
            })
          })
        }).catch((err) => {
          store.dispatch('FedLogOut').then(() => {
            Message.error(err.message || '登录失败，请重新登录')
            next({path: '/'})
          })
        })
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next('/login')
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
