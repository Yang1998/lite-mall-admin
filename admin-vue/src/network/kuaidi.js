import { request } from './request'

export function getKuaidi (id) {
  return request({
    url: `/kuaidi/${id}`,
    method: 'get'
  })
}
