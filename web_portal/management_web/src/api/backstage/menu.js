import request from '@/utils/request'

const menu = "/menu"

export function listPage(param, page) {
  return request({
    url: menu + '/listPage/',
    method: 'post',
    data: {param, page}
  })
}

export function listAll(roleId) {
  return request({
    url: menu + '/listAll/'+roleId,
    method: 'post'
  })
}

export function remove(id) {
  return request({
    url: menu + '/delete/' + id,
    method: 'post'
  })
}

export function save(param) {
  return request({
    url: menu + '/save',
    method: 'post',
    data: {param}
  })
}

export function update(param) {
  return request({
    url: menu + '/update',
    method: 'post',
    data: {param}
  })
}

export function listByLevel(level) {
  return request({
    url: menu + '/listByLevel/' + level,
    method: 'post',
  })
}

export function updateEnableFlag(param) {
  return request({
    url: menu + '/updateEnableFlag',
    method: 'post',
    data: {param}
  })
}


