import request from '@/utils/request'

const invoice="/invoice"

export function save(param){
  return request({
    url: invoice + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: invoice + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: invoice + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: invoice + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
