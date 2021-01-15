package com.ruoyi.framework.datasource.jta.atomikos;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.AtomikosSQLException;
import com.ruoyi.common.enums.XADataSourceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @description:
 *  配置项：
 * {
 *     "driverClassName": "com.mysql.cj.jdbc.Driver",
 *     "initialSize": "10",
 *     "maxActive": "20",
 *     "maxWait": "60000",
 *     "minEvictableIdleTimeMillis": "300000",
 *     "minIdle": "5",
 *     "password": "123456",
 *     "testOnBorrow": "false",
 *     "testOnReturn": "false",
 *     "testWhileIdle": "true",
 *     "timeBetweenEvictionRunsMillis": "60000",
 *     "url": "jdbc:mysql://127.0.0.1:3306/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&poolSize=20",
 *     "username": "root",
 *     "validationQuery": "SELECT 'x'"
 * }
 *
 * @author: zhangxiulin
 * @time: 2021/1/13 15:22
 */
@Component
public class GetDruidXAtomikosDSBStrategy implements IGetAtomikosDataSourceBeanStrategy {

    private static final Logger log = LoggerFactory.getLogger(GetDruidXAtomikosDSBStrategy.class);

    @Override
    public XADataSourceVendor getType() {
        return XADataSourceVendor.DRUIDXA;
    }

    @Override
    public AtomikosDataSourceBean getBean(String uniqueResourceName, Properties properties) throws AtomikosSQLException {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setUniqueResourceName(uniqueResourceName);
        atomikosDataSourceBean.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        atomikosDataSourceBean.setXaProperties(properties);
        // XADataSource无法为Atomikos配置所有必须属性？？？ 额外设置
        atomikosDataSourceBean.setPoolSize(Integer.valueOf(properties.getProperty("maxActive", "20")));
        atomikosDataSourceBean.init();
        return atomikosDataSourceBean;
    }
}
