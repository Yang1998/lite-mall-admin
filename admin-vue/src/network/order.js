import { request } from './request'

export function getOrderList (params) {
  return request({
    url: '/orders',
    method: 'get',
    params: params
  })
}
