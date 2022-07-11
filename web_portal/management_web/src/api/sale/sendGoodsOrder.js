import request from '@/utils/request'

const send_goods_order = "/send_goods_order"

export function save(param) {
  return request({
    url: send_goods_order + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id) {
  return request({
    url: send_goods_order + '/delete/' + id,
    method: 'post'
  })
}

export function update(param) {
  return request({
    url: send_goods_order + '/update',
    method: 'post',
    data: {param}
  })
}

export function listPage(param, page) {
  return request({
    url: send_goods_order + '/listPage',
    method: 'post',
    data: {param, page}
  })
}

export function updateStatus(id, status) {
  return request({
    url: send_goods_order + '/updateStatus/' + id + "/" + status,
    method: 'post',
  })
}
