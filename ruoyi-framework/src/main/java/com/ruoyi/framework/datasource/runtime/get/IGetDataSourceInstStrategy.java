package com.ruoyi.framework.datasource.runtime.get;

import com.ruoyi.common.enums.DataSourceVendor;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 创建数据源实例策略角色
 *
 * @author zhangxiulin
 * @date 2020-12-03
 */
public interface IGetDataSourceInstStrategy {

    DataSourceVendor getType();

    DataSource getInstance(Properties properties);

}
