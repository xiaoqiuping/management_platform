import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '../store'
import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_API,// api的base_url
  timeout: 15000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(config => {
  if(config.method==='get'){
    config.url = config.url + "?t="+new Date().getTime();
  }
  if (store.getters.token) {
    config.headers['Authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  }
  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// response拦截器
service.interceptors.response.use(
    response => {
      if(response.data.type==="application/x-download"){
        return response;
      }
      let res = response.data
      if(res.result){
        if (res.result.code !== 200) {
          // 401:未登录;
          if (res.result.code === 401) {
            MessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
              confirmButtonText: '重新登录',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              store.dispatch('FedLogOut').then(() => {
                location.reload()// 为了重新实例化vue-router对象 避免bug
              })
            })
            return ;
          }
          Message({
            message: res.result.msg,
            type: 'error',
            duration: 3 * 1000
          })
          return Promise.reject("系统错误")
        } else {
          return res
        }
      }else {
        return res;
      }
    },
    error => {
      console.log('错误：' + error.message)// for debug
      Message({
        message: error.message,
        type: 'error',
        duration: 3 * 1000
      })
      return Promise.reject(error)
    }
)

export default service
