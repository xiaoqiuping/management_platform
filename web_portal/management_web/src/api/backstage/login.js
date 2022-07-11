import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/login',
    method: 'post',
    data: {
      param : {
        account:username,
        password:password
      }
    }
  })
}

export function logout() {
  return request({
    url: '/loginOut',
    method: 'post'
  })
}
export function getUserDetail(account) {
  return request({
    url: '/getUserDetail/'+account,
    method: 'get'
  })
}
export function getSiderMenus(account,menuId) {
  return request({
    url: '/getSiderMenus/'+account,
    method: 'post',
    params:{menuId}
  })
}

