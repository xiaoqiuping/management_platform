import request from '@/utils/request'

const saleoff_price="/saleoff_price"

export function save(param){
  return request({
    url: saleoff_price + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: saleoff_price + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: saleoff_price + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: saleoff_price + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
