package com.ruoyi.framework.datasource.jta;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: JTA/XA Atomikos
 * @author: zhangxiulin
 * @time: 2021/1/7 13:47
 */
public class AtomikosDataSourceLayer {

    private static final Logger log = LoggerFactory.getLogger(AtomikosDataSourceLayer.class);

    // ConcurrentHashMap#key不能为null
    public static final Map<String, AtomikosDataSourceBean> DATASOURCE = new ConcurrentHashMap<>();

    public static Connection getConnection(String dataSourceName) {
        try {
            if (!AtomikosDataSourceLayer.DATASOURCE.containsKey(dataSourceName)) {
                return null;
            }
            return AtomikosDataSourceLayer.DATASOURCE.get(dataSourceName).getConnection();
        } catch (SQLException e) {
            log.error("数据库连接获取失败", e);
            return null;
        }
    }

}
