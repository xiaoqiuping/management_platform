import request from '@/utils/request'

const fixed_assets="/fixed_assets"

export function save(param){
  return request({
    url: fixed_assets + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: fixed_assets + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: fixed_assets + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: fixed_assets + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
