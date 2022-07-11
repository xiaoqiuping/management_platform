import request from '@/utils/request'

const semi_manufacture="/semi_manufacture"

export function save(param){
  return request({
    url: semi_manufacture + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: semi_manufacture + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: semi_manufacture + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: semi_manufacture + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
export function updateStatus(param){
  return request({
    url: semi_manufacture + '/updateStatus',
    method: 'post',
    data: {param}
  })
}
