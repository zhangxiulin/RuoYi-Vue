package com.ruoyi.framework.datasource.runtime.get;

import com.ruoyi.common.enums.DataSourceVendor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @description: 创建数据源实例环境角色
 * @author: zhangxiulin
 * @time: 2020/12/13 2:12
 */
@Component
public class GetDataSourceInstContext {

    Map<DataSourceVendor, IGetDataSourceInstStrategy> strategyMap = new HashMap<>();

    public GetDataSourceInstContext(List<IGetDataSourceInstStrategy> strategyList){
        for (IGetDataSourceInstStrategy strategy : strategyList){
            strategyMap.put(strategy.getType(), strategy);
        }
    }

    public DataSource getDataSourceInst(DataSourceVendor type, Properties properties){
        return strategyMap.get(type).getInstance(properties);
    }
}
