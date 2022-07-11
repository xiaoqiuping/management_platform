import request from '@/utils/request'

const supplier="/supplier"

export function save(param){
  return request({
    url: supplier + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: supplier + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: supplier + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: supplier + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
export function get(id){
  return request({
    url: supplier + '/get/'+id,
    method: 'get',
  })
}
