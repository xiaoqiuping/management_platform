import request from '@/utils/request'

const material="/material"

export function save(param){
  return request({
    url: material + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: material + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: material + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: material + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
export function listAll(){
  return request({
    url: material + '/listAll',
    method: 'post'
  })
}
export function listAllByType(param){
  return request({
    url: material + '/listAllByType',
    method: 'post',
    params:param
  })
}
export function listAllByMaterialName(param){
  return request({
    url: material + '/listAllByMaterialName',
    method: 'post',
    data: {param}
  })
}
export function listAllWithOutRawMaterial(page){
  return request({
    url: material + '/listAllWithOutRawMaterial',
    method: 'post',
    data: {page}
  })
}
