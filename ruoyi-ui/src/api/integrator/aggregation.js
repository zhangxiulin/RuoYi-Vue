import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询服务聚合列表
export function listAggregation(query) {
  return request({
    url: '/integrator/aggregation/list',
    method: 'get',
    params: query
  })
}

// 查询服务聚合详细
export function getAggregation(aggrId) {
  return request({
    url: '/integrator/aggregation/' + praseStrEmpty(aggrId),
    method: 'get'
  })
}

// 新增服务聚合
export function addAggregation(data) {
  return request({
    url: '/integrator/aggregation',
    method: 'post',
    data: data
  })
}

// 修改服务聚合
export function updateAggregation(data) {
  return request({
    url: '/integrator/aggregation',
    method: 'put',
    data: data
  })
}

// 删除服务聚合
export function delAggregation(aggrId) {
  return request({
    url: '/integrator/aggregation/' + aggrId,
    method: 'delete'
  })
}

// 导出服务聚合
export function exportAggregation(query) {
  return request({
    url: '/integrator/aggregation/export',
    method: 'get',
    params: query
  })
}
