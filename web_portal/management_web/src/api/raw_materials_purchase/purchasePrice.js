import request from '@/utils/request'

const purchase_price="/purchase_price"

export function save(param){
  return request({
    url: purchase_price + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: purchase_price + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: purchase_price + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: purchase_price + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
