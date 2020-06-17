import { request } from './request'

export function getReportData (id) {
  return request({
    url: `/reports/type/${id}`,
    method: 'get'
  })
}
