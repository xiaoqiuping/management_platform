import request from '@/utils/request'

const raw_material="/raw_material"

export function save(param){
  return request({
    url: raw_material + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: raw_material + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: raw_material + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: raw_material + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
export function updateStatus(param){
  return request({
    url: raw_material + '/updateStatus',
    method: 'post',
    data: {param}
  })
}
