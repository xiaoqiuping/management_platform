import request from '@/utils/request'

const purchase_order = "/purchase_order"

export function save(param) {
  return request({
    url: purchase_order + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id) {
  return request({
    url: purchase_order + '/delete/' + id,
    method: 'post'
  })
}

export function update(param) {
  return request({
    url: purchase_order + '/update',
    method: 'post',
    data: {param}
  })
}

export function listPage(param, page) {
  return request({
    url: purchase_order + '/listPage',
    method: 'post',
    data: {param, page}
  })
}

export function updateStatus(id, type) {
  return request({
    url: purchase_order + '/updateStatus/' + id + "/" + type,
    method: 'post',
  })
}
