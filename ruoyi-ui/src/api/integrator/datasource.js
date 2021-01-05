import request from '@/utils/request'

// 查询数据源列表
export function listDatasource(query) {
  return request({
    url: '/integrator/datasource/list',
    method: 'get',
    params: query
  })
}

// 查询数据源详细
export function getDatasource(datasourceId) {
  return request({
    url: '/integrator/datasource/' + datasourceId,
    method: 'get'
  })
}

// 新增数据源
export function addDatasource(data) {
  return request({
    url: '/integrator/datasource',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function updateDatasource(data) {
  return request({
    url: '/integrator/datasource',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delDatasource(datasourceId) {
  return request({
    url: '/integrator/datasource/' + datasourceId,
    method: 'delete'
  })
}

// 导出数据源
export function exportDatasource(query) {
  return request({
    url: '/integrator/datasource/export',
    method: 'get',
    params: query
  })
}

// 同步数据源
export function synchDs(datasourceId) {
  return request({
    url: '/integrator/datasource/synchDs/' + datasourceId,
    method: 'get'
  })
}
