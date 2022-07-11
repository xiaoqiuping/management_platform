import request from '@/utils/request'

const process="/process"

export function save(param){
  return request({
    url: process + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: process + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: process + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: process + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
export function listAll(){
  return request({
    url: process + '/listAll',
    method: 'post',
  })
}
