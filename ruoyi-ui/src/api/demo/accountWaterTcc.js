import request from '@/utils/request'

// 查询TCC动账流水列表
export function listAccountWaterTcc(query) {
  return request({
    url: '/demo/accountWaterTcc/list',
    method: 'get',
    params: query
  })
}

// 查询TCC动账流水详细
export function getAccountWaterTcc(id) {
  return request({
    url: '/demo/accountWaterTcc/' + id,
    method: 'get'
  })
}

// 新增TCC动账流水
export function addAccountWaterTcc(data) {
  return request({
    url: '/demo/accountWaterTcc',
    method: 'post',
    data: data
  })
}

// 修改TCC动账流水
export function updateAccountWaterTcc(data) {
  return request({
    url: '/demo/accountWaterTcc',
    method: 'put',
    data: data
  })
}

// 删除TCC动账流水
export function delAccountWaterTcc(id) {
  return request({
    url: '/demo/accountWaterTcc/' + id,
    method: 'delete'
  })
}

// 导出TCC动账流水
export function exportAccountWaterTcc(query) {
  return request({
    url: '/demo/accountWaterTcc/export',
    method: 'get',
    params: query
  })
}