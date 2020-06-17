import { request } from './request'

export function getRoleList () {
  return request({
    url: '/roles',
    method: 'get'
  })
}

export function postRole (data) {
  return request({
    url: '/roles',
    method: 'post',
    data: data
  })
}

export function getRoleById (id) {
  return request({
    url: '/roles/' + id,
    method: 'get'
  })
}

export function updateRoleById (role) {
  return request({
    url: '/roles/' + role.id,
    method: 'put',
    data: {
      roleName: role.roleName,
      roleDesc: role.roleDesc
    }
  })
}

export function deleteRoleById (id) {
  return request({
    url: '/roles/' + id,
    method: 'delete'
  })
}

export function removeRight (role, rightId) {
  return request({
    url: `/roles/${role.id}/rights/${rightId}`,
    method: 'delete'
  })
}

export function allocateRight (id, ids) {
  return request({
    url: `/roles/${id}/rights`,
    method: 'post',
    data: {
      rids: ids
    }
  })
}
