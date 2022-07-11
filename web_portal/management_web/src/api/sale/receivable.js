import request from '@/utils/request'

const receivable = "/receivable"

export function save(param) {
  return request({
    url: receivable + '/save',
    method: 'post',
    data: {param}
  })
}

export function remove(id) {
  return request({
    url: receivable + '/delete/' + id,
    method: 'post'
  })
}

export function update(param) {
  return request({
    url: receivable + '/update',
    method: 'post',
    data: {param}
  })
}

export function listPage(param, page) {
  return request({
    url: receivable + '/listPage',
    method: 'post',
    data: {param, page}
  })
}

export function updateStatus(id, status) {
  return request({
    url: receivable + '/updateStatus/' + id + "/" + status,
    method: 'post',
  })
}
