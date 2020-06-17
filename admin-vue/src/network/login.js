import { request } from './request'

export function loginReq (loginFormData) {
  return request({
    url: '/login',
    method: 'post',
    data: loginFormData
  })
}

export function logoutReq () {
  return request({
    url: '/logout',
    method: 'post'
  })
}
