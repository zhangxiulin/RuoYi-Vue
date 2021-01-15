package com.ruoyi.framework.datasource.jta.atomikos;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.AtomikosSQLException;
import com.ruoyi.common.enums.XADataSourceVendor;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2021/1/13 15:22
 */
@Component
public class GetMysqlXAtomikosDSBStrategy implements IGetAtomikosDataSourceBeanStrategy {


    @Override
    public XADataSourceVendor getType() {
        return XADataSourceVendor.MYSQLXA;
    }

    @Override
    public AtomikosDataSourceBean getBean(String uniqueResourceName, Properties properties) throws AtomikosSQLException {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName(uniqueResourceName);
        atomikosDataSourceBean.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");

        // 适配MysqlXADataSource的属性
        Properties mysqlXAdsProperties = new Properties();
        mysqlXAdsProperties.setProperty("user", properties.getProperty("user"));
        mysqlXAdsProperties.setProperty("password", properties.getProperty("password"));
        mysqlXAdsProperties.setProperty("url", properties.getProperty("url"));

        atomikosDataSourceBean.setXaProperties(mysqlXAdsProperties);
        atomikosDataSourceBean.setPoolSize(Integer.valueOf(properties.getProperty("poolSize", "20")));
        atomikosDataSourceBean.init();
        return atomikosDataSourceBean;
    }
}
