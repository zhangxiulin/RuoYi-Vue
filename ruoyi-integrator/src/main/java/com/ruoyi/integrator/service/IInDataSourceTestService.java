package com.ruoyi.integrator.service;

import com.ruoyi.integrator.domain.InDatasource;

/**
 * 测试接口
 */
public interface IInDataSourceTestService {

    /**
     * 查询数据源
     *
     * @param datasourceId 数据源ID
     * @return 数据源
     */
    InDatasource selectInDatasourceById(String datasourceId);

    InDatasource selectInDatasourceByIdConn(String datasourceId);

}
