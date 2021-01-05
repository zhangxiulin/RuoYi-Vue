package com.ruoyi.framework.datasource.runtime.get;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ruoyi.common.enums.DataSourceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @description: 获取Druid数据源
 * @author: zhangxiulin
 * @time: 2020/12/13 2:28
 */
@Component
public class GetDruidDataSourceInstStrategy implements IGetDataSourceInstStrategy {

    private static final Logger log = LoggerFactory.getLogger(GetDruidDataSourceInstStrategy.class);

    @Override
    public DataSourceVendor getType() {
        return DataSourceVendor.Druid;
    }

    @Override
    public DataSource getInstance(Properties properties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();

        dataSource.setDriverClassName(properties.getProperty("driverClassName"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));

        /** 配置初始化大小、最小、最大 */
        dataSource.setInitialSize(Integer.valueOf(properties.getProperty("initialSize", "2")));
        dataSource.setMaxActive(Integer.valueOf(properties.getProperty("maxActive", "10")));
        dataSource.setMinIdle(Integer.valueOf(properties.getProperty("minIdle", "0")));

        /** 配置获取连接等待超时的时间 */
        dataSource.setMaxWait(Long.valueOf(properties.getProperty("maxWait", "0")));

        /** 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */
        dataSource.setTimeBetweenEvictionRunsMillis(Long.valueOf(properties.getProperty("timeBetweenEvictionRunsMillis", "0")));

        /** 配置一个连接在池中最小、最大生存的时间，单位是毫秒 */
        dataSource.setMinEvictableIdleTimeMillis(Long.valueOf(properties.getProperty("minEvictableIdleTimeMillis", "30000")));
        dataSource.setMaxEvictableIdleTimeMillis(Long.valueOf(properties.getProperty("maxEvictableIdleTimeMillis", "30000")));

        /**
         * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
         */
        dataSource.setValidationQuery(properties.getProperty("validationQuery", null));
        /** 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
        dataSource.setTestWhileIdle(Boolean.valueOf(properties.getProperty("testWhileIdle", "false")));
        /** 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        dataSource.setTestOnBorrow(Boolean.valueOf(properties.getProperty("testOnBorrow", "false")));
        /** 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        dataSource.setTestOnReturn(Boolean.valueOf(properties.getProperty("testOnReturn", "false")));

        return dataSource;
    }
}
