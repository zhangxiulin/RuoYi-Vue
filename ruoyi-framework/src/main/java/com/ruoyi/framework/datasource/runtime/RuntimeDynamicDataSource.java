package com.ruoyi.framework.datasource.runtime;

import com.ruoyi.common.core.database.DatabaseAccessLayer;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @description: 运行时动态数据源
 * @author: zhangxiulin
 * @time: 2020/12/11 17:41
 */
public class RuntimeDynamicDataSource  extends AbstractRoutingDataSource {

    public RuntimeDynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources)
    {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey()
    {
        return RuntimeDynamicDataSourceContextHolder.getDataSourceType();
    }

    @Override
    protected DataSource determineTargetDataSource(){
        String dataSourceName = (String) this.determineCurrentLookupKey();
        DataSource dataSource = null;
        if (dataSourceName == null){    // 没有指定dataSourceName，则使用默认数据源，交由父类处理，也可以从DatabaseAccessLayer硬编码指定获取
            dataSource = super.determineTargetDataSource();
        } else {
            dataSource = DatabaseAccessLayer.DATASOURCE.get(dataSourceName);
        }
        return dataSource;
    }

}
