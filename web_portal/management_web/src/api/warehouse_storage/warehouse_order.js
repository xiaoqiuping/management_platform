import request from '@/utils/request'

const warehouse_order="/warehouse_order"

export function save(param){
  return request({
    url: warehouse_order + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: warehouse_order + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: warehouse_order + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: warehouse_order + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
export function updateStatus(param){
  return request({
    url: warehouse_order + '/updateStatus',
    method: 'post',
    data: {param}
  })
}
