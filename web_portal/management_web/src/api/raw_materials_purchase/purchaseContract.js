import request from '@/utils/request'

const purchase_contract="/purchase_contract"

export function save(param){
  return request({
    url: purchase_contract + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: purchase_contract + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: purchase_contract + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: purchase_contract + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
