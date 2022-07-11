import request from '@/utils/request'

const expend_record="/expend_record"

export function save(param){
  return request({
    url: expend_record + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id){
  return request({
    url: expend_record + '/delete/'+id,
    method: 'post'
  })
}
export function update(param){
  return request({
    url: expend_record + '/update',
    method: 'post',
    data: {param}
  })
}
export function listPage(param,page){
  return request({
    url: expend_record + '/listPage',
    method: 'post',
    data: {param,page}
  })
}
