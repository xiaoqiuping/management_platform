import request from '@/utils/request'

const contract="/contract"

export function save(param){
  return request({
    url: contract + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: contract + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: contract + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: contract + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
