import request from '@/utils/request'

const production_process_quality = "/production_process_quality"

export function save(param) {
  return request({
    url: production_process_quality + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id) {
  return request({
    url: production_process_quality + '/delete/' + id,
    method: 'post'
  })
}

export function update(param) {
  return request({
    url: production_process_quality + '/update',
    method: 'post',
    data: {param}
  })
}

export function listPage(param, page) {
  return request({
    url: production_process_quality + '/listPage',
    method: 'post',
    data: {param, page}
  })
}

export function updateStatus(id, status) {
  return request({
    url: production_process_quality + '/updateStatus/' + id + "/" + status,
    method: 'post',
  })
}
