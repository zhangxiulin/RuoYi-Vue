import request from '@/utils/request'

// 查询应用转发权限列表
export function listAppAccessForward(query) {
  return request({
    url: '/integrator/appAccessForward/list',
    method: 'get',
    params: query
  })
}

// 查询应用转发权限详细
export function getAppAccessForward(appFwdId) {
  return request({
    url: '/integrator/appAccessForward/' + appFwdId,
    method: 'get'
  })
}

// 新增应用转发权限
export function addAppAccessForward(data) {
  return request({
    url: '/integrator/appAccessForward',
    method: 'post',
    data: data
  })
}

// 修改应用转发权限
export function updateAppAccessForward(data) {
  return request({
    url: '/integrator/appAccessForward',
    method: 'put',
    data: data
  })
}

// 删除应用转发权限
export function delAppAccessForward(appFwdId) {
  return request({
    url: '/integrator/appAccessForward/' + appFwdId,
    method: 'delete'
  })
}

// 导出应用转发权限
export function exportAppAccessForward(query) {
  return request({
    url: '/integrator/appAccessForward/export',
    method: 'get',
    params: query
  })
}