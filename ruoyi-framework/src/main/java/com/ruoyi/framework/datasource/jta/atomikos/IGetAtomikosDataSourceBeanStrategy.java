package com.ruoyi.framework.datasource.jta.atomikos;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.AtomikosSQLException;
import com.ruoyi.common.enums.XADataSourceVendor;

import javax.sql.XADataSource;
import java.util.Properties;

/**
 * 创建XA数据源实例策略角色
 *
 * @author zhangxiulin
 * @date 2020-12-03
 */
public interface IGetAtomikosDataSourceBeanStrategy {

    XADataSourceVendor getType();

    AtomikosDataSourceBean getBean(String uniqueResourceName, Properties properties) throws AtomikosSQLException;

}
