import request from '@/utils/request'

// 查询应用聚合权限列表
export function listAppAccessAggr(query) {
  return request({
    url: '/integrator/appAccessAggr/list',
    method: 'get',
    params: query
  })
}

// 查询应用聚合权限详细
export function getAppAccessAggr(appAggrId) {
  return request({
    url: '/integrator/appAccessAggr/' + appAggrId,
    method: 'get'
  })
}

// 新增应用聚合权限
export function addAppAccessAggr(data) {
  return request({
    url: '/integrator/appAccessAggr',
    method: 'post',
    data: data
  })
}

// 修改应用聚合权限
export function updateAppAccessAggr(data) {
  return request({
    url: '/integrator/appAccessAggr',
    method: 'put',
    data: data
  })
}

// 删除应用聚合权限
export function delAppAccessAggr(appAggrId) {
  return request({
    url: '/integrator/appAccessAggr/' + appAggrId,
    method: 'delete'
  })
}

// 导出应用聚合权限
export function exportAppAccessAggr(query) {
  return request({
    url: '/integrator/appAccessAggr/export',
    method: 'get',
    params: query
  })
}
