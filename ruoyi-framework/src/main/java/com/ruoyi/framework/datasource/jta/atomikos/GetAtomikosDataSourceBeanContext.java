package com.ruoyi.framework.datasource.jta.atomikos;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.AtomikosSQLException;
import com.ruoyi.common.enums.XADataSourceVendor;
import org.springframework.stereotype.Component;

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
public class GetAtomikosDataSourceBeanContext {

    Map<XADataSourceVendor, IGetAtomikosDataSourceBeanStrategy> strategyMap = new HashMap<>();

    public GetAtomikosDataSourceBeanContext(List<IGetAtomikosDataSourceBeanStrategy> strategyList){
        for (IGetAtomikosDataSourceBeanStrategy strategy : strategyList){
            strategyMap.put(strategy.getType(), strategy);
        }
    }

    public AtomikosDataSourceBean getAtomikosDataSourceBean(XADataSourceVendor type,String uniqueResourceName,  Properties properties) throws AtomikosSQLException {
        return strategyMap.get(type).getBean(uniqueResourceName, properties);
    }
}
