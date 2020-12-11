package com.ruoyi.integrator.mapper;

import java.util.List;
import com.ruoyi.integrator.domain.InDatasource;

/**
 * 数据源MapperAuto接口
 *
 * 由代码生成器产生，为适应表结构变化，请勿修改
 *
 * @author zhangxiulin
 * @date 2020-12-09
 */
public interface InDatasourceMapperAuto
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
     * 删除数据源
     * 
     * @param datasourceId 数据源ID
     * @return 结果
     */
    public int deleteInDatasourceById(String datasourceId);

    /**
     * 批量删除数据源
     * 
     * @param datasourceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteInDatasourceByIds(String[] datasourceIds);
}
