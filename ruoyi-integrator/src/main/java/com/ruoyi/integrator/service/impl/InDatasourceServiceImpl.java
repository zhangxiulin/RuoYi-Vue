package com.ruoyi.integrator.service.impl;

import java.util.List;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.database.DatabaseAccessLayer;
import com.ruoyi.common.enums.DataSourceVendor;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PropertiesUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.framework.datasource.runtime.get.GetDataSourceInstContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.integrator.mapper.InDatasourceMapper;
import com.ruoyi.integrator.domain.InDatasource;
import com.ruoyi.integrator.service.IInDatasourceService;

import javax.sql.DataSource;

/**
 * 数据源Service业务层处理
 * 
 * @author zhangxiulin
 * @date 2020-12-09
 */
@Service
public class InDatasourceServiceImpl implements IInDatasourceService 
{

    private static final Logger log = LoggerFactory.getLogger(InDatasourceServiceImpl.class);

    @Autowired
    private InDatasourceMapper inDatasourceMapper;

    @Autowired
    private GetDataSourceInstContext getDataSourceInstContext;

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

    @Override
    public boolean synchDs(String datasourceId) {
        /* 目前只支持单体架构 */
        // 1. 获取数据源配置
        // 2. 判断数据源的状态，激活则插入或更新数据源容器，禁用则从数据源容器中移除
        InDatasource inDatasource = this.selectInDatasourceById(datasourceId);
        if (inDatasource != null){
            String datasourceName = inDatasource.getDatasourceName();
            String datasourceOptions = inDatasource.getDatasourceOptions();
            String datasourceType = inDatasource.getDatasourceType();
            if (Constants.STATUS_NORMAL.equals(inDatasource.getStatus())){
                log.info("开始同步数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] ...");
                DataSourceVendor dataSourceVendorEnum = null;
                try {
                    dataSourceVendorEnum = DataSourceVendor.valueOf(datasourceType);
                } catch (Throwable throwable){
                    log.error("数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 异常！不支持的类型" , throwable);
                    return false;
                }
                JSONObject datasourceOptionsJson = null;
                try {
                    datasourceOptionsJson = JSON.parseObject(datasourceOptions);
                } catch (Throwable throwable){
                    log.error("数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 异常！配置项不是JSON格式", throwable);
                    return false;
                }
                // 转JSON格式配置项为Properties
                Properties datasourceProperties = null;
                try {
                    if (datasourceOptionsJson != null){
                        datasourceProperties = PropertiesUtils.json2Properties(datasourceOptionsJson);
                    }
                } catch (Throwable throwable){
                    log.error("数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 异常！配置项格式不正确", throwable);
                    return false;
                }
                // 加载数据源
                try {
                    DataSource dataSourceInst = getDataSourceInstContext.getDataSourceInst(dataSourceVendorEnum, datasourceProperties);
                    DatabaseAccessLayer.DATASOURCE.put(datasourceName, dataSourceInst);
                } catch (Throwable throwable){
                    log.error("数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 异常！加载数据源异常", throwable);
                    return false;
                }
                log.info("同步数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 结束.");
            } else {
                log.info("开始移除数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] ...");
                DatabaseAccessLayer.DATASOURCE.remove(datasourceName);
                log.info("移除数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 结束.");
            }
        }
        return true;
    }
}
