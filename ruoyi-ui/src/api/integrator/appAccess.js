import request from '@/utils/request'

// 查询接入应用列表
export function listAppAccess(query) {
  return request({
    url: '/integrator/appAccess/list',
    method: 'get',
    params: query
  })
}

// 查询接入应用详细
export function getAppAccess(appId) {
  return request({
    url: '/integrator/appAccess/' + appId,
    method: 'get'
  })
}

// 新增接入应用
export function addAppAccess(data) {
  return request({
    url: '/integrator/appAccess',
    method: 'post',
    data: data
  })
}

// 修改接入应用
export function updateAppAccess(data) {
  return request({
    url: '/integrator/appAccess',
    method: 'put',
    data: data
  })
}

// 删除接入应用
export function delAppAccess(appId) {
  return request({
    url: '/integrator/appAccess/' + appId,
    method: 'delete'
  })
}

// 导出接入应用
export function exportAppAccess(query) {
  return request({
    url: '/integrator/appAccess/export',
    method: 'get',
    params: query
  })
}