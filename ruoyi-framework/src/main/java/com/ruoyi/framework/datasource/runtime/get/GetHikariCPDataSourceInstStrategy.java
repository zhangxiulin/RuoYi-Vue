package com.ruoyi.framework.datasource.runtime.get;

import com.ruoyi.common.enums.DataSourceVendor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @description: 获取HikariCP数据源
 * @author: zhangxiulin
 * @time: 2020/12/13 2:33
 */
@Component
public class GetHikariCPDataSourceInstStrategy implements IGetDataSourceInstStrategy {

    @Override
    public DataSourceVendor getType() {
        return DataSourceVendor.HikariCP;
    }

    @Override
    public DataSource getInstance(Properties properties) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty("driverClassName"));
        config.setJdbcUrl(properties.getProperty("url"));
        config.setUsername(properties.getProperty("username"));
        config.setPassword(properties.getProperty("password"));
        config.setAutoCommit(
                Boolean.valueOf(properties.getProperty("autoCommit") == null ? "true" : properties.getProperty("autoCommit")));
//					config.setConnectionInitSql(
//							dm.xadatasource("conn.init.sql") == null ? "" : dm.xadatasource("conn.init.sql").toString());
        config.setIdleTimeout(
                Long.valueOf(properties.getProperty("idleTimeout") == null ? "30000" : properties.getProperty("idleTimeout")));
        config.setPoolName(properties.getProperty("poolName") == null ? "default" : properties.getProperty("poolName"));
        config.setRegisterMbeans(Boolean
                .valueOf(properties.getProperty("registerMbean") == null ? "true" : properties.getProperty("registerMbean")));
        config.setCatalog(properties.getProperty("catalog") == null ? "" : properties.getProperty("catalog"));
        config.setMaximumPoolSize(Integer.valueOf(properties.getProperty("maximumPoolSize") == null ? "2" : properties.getProperty("maximumPoolSize")));
        config.setMinimumIdle(Integer.valueOf(properties.getProperty("minimumIdle") == null ? "2" : properties.getProperty("minimumIdle")));

        return new HikariDataSource(config);
    }

}
