import 'normalize.css/normalize.css'// A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n
import VCharts from 'v-charts'


import '@/styles/index.scss' // global css

//权限控制，未登录跳转登录界面
import '@/permission'
import '@/icons' // icon
import '@/utils/filter'

Vue.use(ElementUI, { locale })
Vue.use(VCharts)

import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'

Vue.config.productionTip = false

Vue.filter("formatNum",function (num){
  return parseFloat(num).toFixed(2)
})

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
