import request from '@/utils/request'

const role = "/role"

export function listAll() {
  return request({
    url: role + '/listAll',
    method: 'post'
  })
}

export function listPage(param, page) {
  return request({
    url: role + '/listPage',
    method: 'post',
    data: {param, page}
  })
}


export function save(param) {
  return request({
    url: role + '/save',
    method: 'post',
    data: {param}
  })
}

export function update(param) {
  return request({
    url: role + '/update',
    method: 'post',
    data: {param}
  })
}

export function updateEnableFlag(param) {
  return request({
    url: role + '/updateEnableFlag',
    method: 'post',
    data: {param}
  })
}

export function remove(id) {
  return request({
    url: role + '/delete/' + id,
    method: 'post'
  })
}

export function allocMenu(id,menuIds) {
  return request({
    url: role + '/allocMenu/' + id,
    method: 'post',
    params:menuIds
  })
}

export function listMenuByRoleId(roleId) {
  return request({
    url: role + '/listMenu/' + roleId,
    method: 'get'
  })
}

