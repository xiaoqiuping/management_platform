import request from '@/utils/request'

const outsource_order = "/outsource_order"

export function save(param) {
  return request({
    url: outsource_order + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id) {
  return request({
    url: outsource_order + '/delete/' + id,
    method: 'post'
  })
}

export function update(param) {
  return request({
    url: outsource_order + '/update',
    method: 'post',
    data: {param}
  })
}

export function listPage(param, page) {
  return request({
    url: outsource_order + '/listPage',
    method: 'post',
    data: {param, page}
  })
}

export function updateStatus(id, status) {
  return request({
    url: outsource_order + '/updateStatus/' + id + "/" + status,
    method: 'post',
  })
}
