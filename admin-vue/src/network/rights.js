import { request } from './request'

export function getAllRights (type) {
  return request({
    url: '/rights/' + type,
    method: 'get'
  })
}
