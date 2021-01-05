package com.ruoyi.common.core.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/14 11:28
 */
public class DatabaseAccessLayer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseAccessLayer.class);

    // ConcurrentHashMap#key不能为null
    public static final Map<String, DataSource> DATASOURCE = new ConcurrentHashMap<>();

    public static Connection getConnection(String dataSourceName) {
        try {
            if (!DatabaseAccessLayer.DATASOURCE.containsKey(dataSourceName)) {
                return null;
            }
            return DatabaseAccessLayer.DATASOURCE.get(dataSourceName).getConnection();
        } catch (SQLException e) {
            log.error("数据库连接获取失败", e);
            return null;
        }
    }

}
