import request from '@/utils/request'

// 查询转发配置列表
export function listForwardInfo(query) {
  return request({
    url: '/integrator/forwardInfo/list',
    method: 'get',
    params: query
  })
}

// 查询转发配置详细
export function getForwardInfo(infoId) {
  return request({
    url: '/integrator/forwardInfo/' + infoId,
    method: 'get'
  })
}

// 新增转发配置
export function addForwardInfo(data) {
  return request({
    url: '/integrator/forwardInfo',
    method: 'post',
    data: data
  })
}

// 修改转发配置
export function updateForwardInfo(data) {
  return request({
    url: '/integrator/forwardInfo',
    method: 'put',
    data: data
  })
}

// 删除转发配置
export function delForwardInfo(infoId) {
  return request({
    url: '/integrator/forwardInfo/' + infoId,
    method: 'delete'
  })
}

// 导出转发配置
export function exportForwardInfo(query) {
  return request({
    url: '/integrator/forwardInfo/export',
    method: 'get',
    params: query
  })
}