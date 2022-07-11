import request from '@/utils/request'

const log = "/log"

export function listPage(param, page) {
  return request({
    url: log + '/listPage/',
    method: 'post',
    data: {param, page}
  })
}
