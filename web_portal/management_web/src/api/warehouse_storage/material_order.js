import request from '@/utils/request'

const material_order="/material_order"

export function save(param){
  return request({
    url: material_order + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: material_order + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: material_order + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: material_order + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
export function get(id){
  return request({
    url: material_order + '/get/'+id,
    method: 'get',
  })
}
