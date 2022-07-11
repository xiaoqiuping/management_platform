import request from '@/utils/request'

const repair_order = "/repair_order"

export function save(param) {
  return request({
    url: repair_order + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id) {
  return request({
    url: repair_order + '/delete/' + id,
    method: 'post'
  })
}
export function update(param) {
  return request({
    url: repair_order + '/update',
    method: 'post',
    data: {param}
  })
}
export function examineOrder(param) {
  return request({
    url: repair_order + '/examineOrder',
    method: 'post',
    data: {param}
  })
}


export function listPage(param, page) {
  return request({
    url: repair_order + '/listPage',
    method: 'post',
    data: {param, page}
  })
}

export function updateStatus(id, status) {
  return request({
    url: repair_order + '/updateStatus/' + id + "/" + status,
    method: 'post',
  })
}
