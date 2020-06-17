import { request } from './request'

export function getParamsList (id, params) {
  return request({
    url: `/categories/${id}/attributes`,
    method: 'get',
    params: params
  })
}

export function addParams (id, data) {
  return request({
    url: `/categories/${id}/attributes`,
    method: 'post',
    data: data
  })
}

export function getParamsInfo (id, attrId, params) {
  return request({
    url: `/categories/${id}/attributes/${attrId}`,
    method: 'get',
    params: params
  })
}

export function updateParamsInfo (id, attrId, data) {
  return request({
    url: `/categories/${id}/attributes/${attrId}`,
    method: 'put',
    data: data
  })
}

export function deleteParams (id, attrId) {
  return request({
    url: `/categories/${id}/attributes/${attrId}`,
    method: 'delete'
  })
}
