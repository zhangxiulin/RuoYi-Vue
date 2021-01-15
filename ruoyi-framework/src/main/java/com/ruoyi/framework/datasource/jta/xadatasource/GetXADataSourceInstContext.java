package com.ruoyi.framework.datasource.jta.xadatasource;

import com.ruoyi.common.enums.XADataSourceVendor;
import org.springframework.stereotype.Component;

import javax.sql.XADataSource;
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
public class GetXADataSourceInstContext {

    Map<XADataSourceVendor, IGetXADataSourceInstStrategy> strategyMap = new HashMap<>();

    public GetXADataSourceInstContext(List<IGetXADataSourceInstStrategy> strategyList){
        for (IGetXADataSourceInstStrategy strategy : strategyList){
            strategyMap.put(strategy.getType(), strategy);
        }
    }

    public XADataSource getXADataSourceInst(XADataSourceVendor type, Properties properties){
        return strategyMap.get(type).getInstance(properties);
    }
}
