import request from '@/utils/request'

const account = "/account"

export function fetchList(page,param) {
  return request({
    url: account + '/list',
    method: 'post',
    data: {page,param}
  })
}

export function createAccount(param) {
  return request({
    url: account + '/save',
    method: 'post',
    data: {param}
  })
}

export function updateAccount(param) {
  return request({
    url: account + '/update',
    method: 'post',
    data: {param}
  })
}

export function updatePassword(param) {
  return request({
    url: account + '/updatePassword',
    method: 'post',
    data: {param}
  })
}

export function updateDeleteFlag(param) {
  return request({
    url: account + '/updateDeleteFlag',
    method: 'post',
    data: {param}
  })
}

export function deleteAccount(id) {
  return request({
    url: account + '/delete/'+id,
    method: 'post'
  })
}

export function getRoleByAccount(roleId) {
  return request({
    url: account + '/getRoleByAccount/'+roleId,
    method: 'post'
  })
}

export function allocRole(accountId, roleIds) {
  return request({
    url: account + '/allocRole/'+accountId,
    method: 'post',
    params:roleIds
  })
}

