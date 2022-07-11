import request from '@/utils/request'

const production_order = "/production_order"

export function save(param) {
  return request({
    url: production_order + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id) {
  return request({
    url: production_order + '/delete/' + id,
    method: 'post'
  })
}

export function update(param) {
  return request({
    url: production_order + '/update',
    method: 'post',
    data: {param}
  })
}

export function listPage(param, page) {
  return request({
    url: production_order + '/listPage',
    method: 'post',
    data: {param, page}
  })
}
export function listPageByFrom(param, page,from) {
  return request({
    url: production_order + '/listPage/'+from,
    method: 'post',
    data: {param, page}
  })
}
export function updateStatus(id, status) {
  return request({
    url: production_order + '/updateStatus/' + id + '/' + status,
    method: 'post',
  })
}
export function verifyOrder(param) {
  return request({
    url: production_order + '/verifyOrder',
    method: 'post',
    data: {param}
  })
}
export function examineOrder(param) {
  return request({
    url: production_order + '/examineOrder',
    method: 'post',
    data: {param}
  })
}
export function examineProcess(param) {
  return request({
    url: production_order + '/examineProcess',
    method: 'post',
    data: {param}
  })
}
export function updateProductionProcess(param) {
  return request({
    url: production_order + '/updateProductionProcess',
    method: 'post',
    data: {param}
  })
}
