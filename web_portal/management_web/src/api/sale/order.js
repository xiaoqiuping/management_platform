import request from '@/utils/request'

const sale_order="/sale_order"

export function save(param){
  return request({
    url: sale_order + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: sale_order + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: sale_order + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: sale_order + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
export function updateStatus(param){
  return request({
    url: sale_order + '/updateStatus',
    method: 'post',
    data: {param}
  })
}
