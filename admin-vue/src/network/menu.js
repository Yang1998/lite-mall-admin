import { request } from './request'

export function getMenusList () {
  return request({
    url: '/menus',
    method: 'get'
  })
}
