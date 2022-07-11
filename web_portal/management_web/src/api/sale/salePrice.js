import request from '@/utils/request'

const sale_price="/sale_price"

export function save(param){
  return request({
    url: sale_price + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: sale_price + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: sale_price + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: sale_price + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
