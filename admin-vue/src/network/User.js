import { request } from './request'

export function userListReq (params) {
  return request({
    url: '/users',
    method: 'get',
    params: params
  })
}

export function changeUserState (userInfo) {
  return request({
    url: `/users/${userInfo.id}/state/${userInfo.state}`,
    method: 'put'
  })
}

export function createUser (userInfo) {
  return request({
    url: '/users',
    method: 'post',
    data: userInfo
  })
}

export function getUserInfoById (id) {
  return request({
    url: '/users/' + id,
    method: 'get'
  })
}

export function editUserInfo (userInfo) {
  return request({
    url: '/users/' + userInfo.id,
    method: 'put',
    data: {
      email: userInfo.email,
      mobile: userInfo.mobile
    }
  })
}

export function deleteUser (id) {
  return request({
    url: '/users/' + id,
    method: 'delete'
  })
}

export function setUserRoleId (id, data) {
  return request({
    url: `/users/${id}/role`,
    method: 'put',
    data: data
  })
}
