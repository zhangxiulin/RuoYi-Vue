package com.ruoyi.integrator.service;

import java.util.List;
import com.ruoyi.integrator.domain.InDatasource;

/**
 * 数据源Service接口
 * 
 * @author zhangxiulin
 * @date 2020-12-09
 */
public interface IInDatasourceService 
{
    /**
     * 查询数据源
     * 
     * @param datasourceId 数据源ID
     * @return 数据源
     */
    public InDatasource selectInDatasourceById(String datasourceId);

    /**
     * 查询数据源列表
     * 
     * @param inDatasource 数据源
     * @return 数据源集合
     */
    public List<InDatasource> selectInDatasourceList(InDatasource inDatasource);

    /**
     * 新增数据源
     * 
     * @param inDatasource 数据源
     * @return 结果
     */
    public int insertInDatasource(InDatasource inDatasource);

    /**
     * 修改数据源
     * 
     * @param inDatasource 数据源
     * @return 结果
     */
    public int updateInDatasource(InDatasource inDatasource);

    /**
     * 批量删除数据源
     * 
     * @param datasourceIds 需要删除的数据源ID
     * @return 结果
     */
    public int deleteInDatasourceByIds(String[] datasourceIds);

    /**
     * 删除数据源信息
     * 
     * @param datasourceId 数据源ID
     * @return 结果
     */
    public int deleteInDatasourceById(String datasourceId);

    /**
     *@Description: 同步数据源
     *@params: datasourceId 数据源ID
     *@return:
     */
    boolean synchDs(String datasourceId);

    /**
     *@Description: 同步Atomikos JTA/XA数据源
     *@params: [datasourceId]
     *@return: boolean
     */
    boolean synchAtomikos(String datasourceId);
}
