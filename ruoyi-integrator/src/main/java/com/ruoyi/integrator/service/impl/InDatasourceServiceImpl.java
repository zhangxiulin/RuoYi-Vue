package com.ruoyi.integrator.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.integrator.mapper.InDatasourceMapper;
import com.ruoyi.integrator.domain.InDatasource;
import com.ruoyi.integrator.service.IInDatasourceService;

/**
 * 数据源Service业务层处理
 * 
 * @author zhangxiulin
 * @date 2020-12-09
 */
@Service
public class InDatasourceServiceImpl implements IInDatasourceService 
{
    @Autowired
    private InDatasourceMapper inDatasourceMapper;

    /**
     * 查询数据源
     * 
     * @param datasourceId 数据源ID
     * @return 数据源
     */
    @Override
    public InDatasource selectInDatasourceById(String datasourceId)
    {
        return inDatasourceMapper.selectInDatasourceById(datasourceId);
    }

    /**
     * 查询数据源列表
     * 
     * @param inDatasource 数据源
     * @return 数据源
     */
    @Override
    public List<InDatasource> selectInDatasourceList(InDatasource inDatasource)
    {
        return inDatasourceMapper.selectInDatasourceList(inDatasource);
    }

    /**
     * 新增数据源
     * 
     * @param inDatasource 数据源
     * @return 结果
     */
    @Override
    public int insertInDatasource(InDatasource inDatasource)
    {
        inDatasource.setDatasourceId(IdUtils.fastSimpleUUID());
        inDatasource.setCreateBy(SecurityUtils.getUsername());
        inDatasource.setCreateTime(DateUtils.getNowDate());
        return inDatasourceMapper.insertInDatasource(inDatasource);
    }

    /**
     * 修改数据源
     * 
     * @param inDatasource 数据源
     * @return 结果
     */
    @Override
    public int updateInDatasource(InDatasource inDatasource)
    {
        inDatasource.setUpdateTime(DateUtils.getNowDate());
        inDatasource.setUpdateBy(SecurityUtils.getUsername());
        return inDatasourceMapper.updateInDatasource(inDatasource);
    }

    /**
     * 批量删除数据源
     * 
     * @param datasourceIds 需要删除的数据源ID
     * @return 结果
     */
    @Override
    public int deleteInDatasourceByIds(String[] datasourceIds)
    {
        return inDatasourceMapper.deleteInDatasourceByIds(datasourceIds);
    }

    /**
     * 删除数据源信息
     * 
     * @param datasourceId 数据源ID
     * @return 结果
     */
    @Override
    public int deleteInDatasourceById(String datasourceId)
    {
        return inDatasourceMapper.deleteInDatasourceById(datasourceId);
    }
}
