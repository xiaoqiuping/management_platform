import request from '@/utils/request'

const miscellaneous_material="/miscellaneous_material"

export function save(param){
  return request({
    url: miscellaneous_material + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: miscellaneous_material + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: miscellaneous_material + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: miscellaneous_material + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
