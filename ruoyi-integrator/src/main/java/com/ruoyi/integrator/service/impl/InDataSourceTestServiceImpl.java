package com.ruoyi.integrator.service.impl;

import com.ruoyi.common.annotation.RuntimeDataSource;
import com.ruoyi.common.core.database.DatabaseAccessLayer;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.integrator.domain.InDatasource;
import com.ruoyi.integrator.mapper.InDatasourceMapper;
import com.ruoyi.integrator.service.IInDataSourceTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/15 12:48
 */
@Service
public class InDataSourceTestServiceImpl implements IInDataSourceTestService {

    private static final Logger log = LoggerFactory.getLogger(InDataSourceTestServiceImpl.class);

    @Autowired
    private InDatasourceMapper inDatasourceMapper;

    @Override
    @RuntimeDataSource("ruoyi")
    public InDatasource selectInDatasourceById(String datasourceId) {
        return inDatasourceMapper.selectInDatasourceById(datasourceId);
    }

    @Override
    public InDatasource selectInDatasourceByIdConn(String datasourceId) {
        InDatasource inDatasource = new InDatasource();
        // 获取Connection
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DatabaseAccessLayer.getConnection("ruoyi");
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("select datasource_id, datasource_name as name, create_time as createTime from in_datasource where datasource_id = ?");
            ps.setString(1, datasourceId);
            rs = ps.executeQuery();
            Map<String, Integer> nameColMap = new HashMap<>();
            Map<String, Integer> labelColMap = new HashMap<>();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i=0,cc=rsmd.getColumnCount(); i < cc; i++){
                nameColMap.put(rsmd.getColumnName(i+1), i);
                labelColMap.put(rsmd.getColumnLabel(i+1), i);
            }
            log.info("nameColMap:" + nameColMap);
            log.info("labelColMap:" + labelColMap);
            if (rs != null){
                rs.next();
                inDatasource.setDatasourceId(rs.getString("datasource_id"));
                inDatasource.setDatasourceName(rs.getString("name"));
                inDatasource.setCreateTime((Date) rs.getObject("createTime"));
            }
        } catch (SQLException e) {
            String errMsg = "数据库操作异常";
            log.error(errMsg, e);
            throw new CustomException(errMsg);
        } finally {
            // 释放资源
            if (rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps == null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn == null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return inDatasource;
    }

}
