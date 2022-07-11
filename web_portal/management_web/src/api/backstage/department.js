import request from '@/utils/request'

const department = "/department"

export function listAll() {
  return request({
    url: department + '/list',
    method: 'post'
  })
}

export function listPage(param, page) {
  return request({
    url: department + '/listPage',
    method: 'post',
    data: {param, page}
  })
}

export function save(param) {
  return request({
    url: department + '/save',
    method: 'post',
    data: {param}
  })
}

export function update(param) {
  return request({
    url: department + '/update',
    method: 'post',
    data: {param}
  })
}

export function remove(id) {
  return request({
    url: department + '/delete/' + id,
    method: 'post'
  })
}
