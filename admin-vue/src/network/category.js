import { request } from './request'

// 必须指定type(1-3)字段
export function getAllCates (params) {
  return request({
    url: '/categories',
    method: 'get',
    params: params
  })
}

export function addCategory (body) {
  return request({
    url: 'categories',
    method: 'post',
    data: body
  })
}

export function getCategoryById (id) {
  return request({
    url: `/categories/${id}`,
    method: 'get'
  })
}

export function updateCategory (id, data) {
  return request({
    url: `/categories/${id}`,
    method: 'put',
    data: data
  })
}

export function deleteCategory (id) {
  return request({
    url: `/categories/${id}`,
    method: 'delete'
  })
}
