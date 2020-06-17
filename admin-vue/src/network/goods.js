import { request } from './request'

export function getAllGoods (params) {
  return request({
    url: '/goods',
    method: 'get',
    params: params
  })
}

export function deleteGoods (id) {
  return request({
    url: `/goods/${id}`,
    method: 'delete'
  })
}

// 后台注意 => 商品名要唯一
export function addGoods (form) {
  return request({
    url: '/goods',
    method: 'post',
    data: form
  })
}
