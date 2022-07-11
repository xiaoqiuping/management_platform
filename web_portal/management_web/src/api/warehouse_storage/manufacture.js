import request from '@/utils/request'

const manufacture="/manufacture"

export function save(param){
  return request({
    url: manufacture + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: manufacture + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: manufacture + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: manufacture + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
export function updateStatus(param){
  return request({
    url: manufacture + '/updateStatus',
    method: 'post',
    data: {param}
  })
}
