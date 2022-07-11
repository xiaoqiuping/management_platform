import request from '@/utils/request'

const raw_material_quality = "/raw_material_quality"

export function save(param) {
  return request({
    url: raw_material_quality + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id) {
  return request({
    url: raw_material_quality + '/delete/' + id,
    method: 'post'
  })
}

export function update(param) {
  return request({
    url: raw_material_quality + '/update',
    method: 'post',
    data: {param}
  })
}

export function listPage(param, page) {
  return request({
    url: raw_material_quality + '/listPage',
    method: 'post',
    data: {param, page}
  })
}

export function updateStatus(id, status) {
  return request({
    url: raw_material_quality + '/updateStatus/' + id + "/" + status,
    method: 'post',
  })
}
