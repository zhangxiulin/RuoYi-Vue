package com.ruoyi.common.core.database.ops;

import com.ruoyi.common.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/15 23:34
 */
public class TableService {

    private static final Logger log = LoggerFactory.getLogger(TableService.class);

    public TableService(){}

    public Table query(Connection conn, String sql) throws Exception {
        long s1 = System.currentTimeMillis();
        long s2 = 0L;
        log.debug("数据库查询操作开始执行：" + sql);

        ResultSet rs = null;
        Statement stmt = null;

        Table table = new Table();
        try {
            stmt = this.createStmt(conn, true);
            rs = stmt.executeQuery(sql);
            if (rs != null) {
                table = table.addColumns(TableUtils.mapColumns(rs, new HashMap()));
                s2 = System.currentTimeMillis();
                Object[][] r = TableUtils.mapValues(rs, table.getColMap().size());
                if (r != null && r.length > 0) {
                    table = table.addData(r);
                }
                r = null;
            }
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        } catch (SQLException var12) {
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
            log.error("映射数据时发生错误。", var12);
        } catch (Exception var13) {
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
            log.warn("映射数据时发生错误，有可能是因为空表造成的，若是空表引起出错，请忽略。", var13);
        }

        long s3 = System.currentTimeMillis();
        log.debug("映射数据结构耗时：" + (s2 - s1) + "ms");
        log.debug("映射数据耗时：" + (s3 - s2) + "ms");
        log.debug("数据映射总耗时：" + (s3 - s1) + "ms");
        return table;
    }

    /*
      * PreparedStatement只能用来为可以加引号’的参数（如参数值）设置动态参数，即用?占位，不可用于表名、字段名等，不然怎么生成预编译的语句对象呢
      * 所以sql中必须知道要查询或操作那些字段
     */
    public Table queryPr(Connection conn, String sql, Object[] params) throws Exception {
        long s1 = System.currentTimeMillis();
        long s2 = 0L;
        log.debug("数据库查询操作开始执行：" + sql);

        ResultSet rs = null;
        PreparedStatement stmt = null;

        Table table = new Table();
        try {
            stmt = this.createPreparedStmt(conn, true, sql);
            if (params != null){
                for (int i=0,len=params.length; i<len; i++){
                    stmt.setObject(i+1, params[i]);
                }
            }
            rs =  stmt.executeQuery();
            if (rs != null) {
                table = table.addColumns(TableUtils.mapColumns(rs, new HashMap()));
                s2 = System.currentTimeMillis();
                Object[][] r = TableUtils.mapValues(rs, table.getColMap().size());
                if (r != null && r.length > 0) {
                    table = table.addData(r);
                }

                r = null;
            }

            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        } catch (SQLException var12) {
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
            log.error("映射数据时发生错误。", var12);
        } catch (Exception var13) {
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
            log.warn("映射数据时发生错误，有可能是因为空表造成的，若是空表引起出错，请忽略。", var13);
        }

        long s3 = System.currentTimeMillis();
        log.debug("映射数据结构耗时：" + (s2 - s1) + "ms");
        log.debug("映射数据耗时：" + (s3 - s2) + "ms");
        log.debug("数据映射总耗时：" + (s3 - s1) + "ms");
        return table;
    }

    public boolean updateTx(Connection conn, String... sqls) {
        Statement stmt = this.createStmt(conn, false);
        log.debug("数据库更新操作批处理开始执行，执行语句数：" + (sqls != null ? sqls.length : "0"));
        Savepoint sp = null;

        try {
            conn.setAutoCommit(false);
            sp = conn.setSavepoint("update_" + RandomUtils.genRandomString("yyyyMMddHHmmss", 0));
            log.debug("已生成回退节点：" + sp.getSavepointName());
            String[] var8 = sqls;
            int var7 = sqls.length;

            for(int var6 = 0; var6 < var7; ++var6) {
                String sql = var8[var6];
                stmt.addBatch(sql);
                log.debug("添加操作语句至批处理：" + sql);
            }

            stmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            stmt.close();
            stmt = null;
            return true;
        } catch (Exception var10) {
            try {
                conn.rollback(sp);
                stmt.close();
                stmt = null;
            } catch (SQLException var9) {
                stmt = null;
                log.error("数据库更新执行操作失败，回滚数据失败", var10);
                return false;
            }

            log.error("数据库更新执行操作失败，已回滚", var10);
            return false;
        }
    }

    public boolean updateTxPr(Connection conn, String sql, List<Object[]> params){
        PreparedStatement stmt = this.createPreparedStmt(conn, false, sql);
        log.debug("数据库更新操作批处理开始执行：" + sql);
        Savepoint sp = null;

        try {
            conn.setAutoCommit(false);
            sp = conn.setSavepoint("update_" + RandomUtils.genRandomString("yyyyMMddHHmmss", 0));
            log.debug("已生成回退节点：" + sp.getSavepointName());
            for (int i=0,len=params.size(); i<len; i++){
                Object[] o = params.get(i);
                for (int j=0,lenJ=o.length; j<lenJ; j++){
                    stmt.setObject(j+1, o[j]);
                }
                stmt.addBatch();
            }

            stmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            stmt.close();
            stmt = null;
            return true;
        } catch (Exception var10) {
            try {
                conn.rollback(sp);
                stmt.close();
                stmt = null;
            } catch (SQLException var9) {
                stmt = null;
                log.error("数据库更新执行操作失败，回滚数据失败", var10);
                return false;
            }

            log.error("数据库更新执行操作失败，已回滚", var10);
            return false;
        }
    }

    public boolean update(Connection conn, String... sqls) {
        Statement stmt = this.createStmt(conn, false);
        log.debug("数据库更新操作批处理开始执行，执行语句数：" + (sqls != null ? sqls.length : "0"));

        try {
            String[] var7 = sqls;
            int var6 = sqls.length;

            for(int var5 = 0; var5 < var6; ++var5) {
                String sql = var7[var5];
                stmt.addBatch(sql);
                log.debug("添加操作语句至批处理：" + sql);
            }

            stmt.executeBatch();
            stmt.close();
            stmt = null;
            return true;
        } catch (Exception var19) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException var18) {
                stmt = null;
                log.error("数据库更新执行操作失败", var19);
                return false;
            }

            log.error("数据库更新执行操作失败,数据回退", var19);
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (SQLException var17) {
                log.debug("设置数据库连接自动提交失败", var17);
                stmt = null;
            }

        }
    }

    /**
     * 如果PreparedStatement对象初始化时resultSetType参数设置为TYPE_FORWARD_ONLY，
     * 在从ResultSet（结果集）中读取记录的时，对于访问过的记录就自动释放了内存。
     * 而设置为TYPE_SCROLL_INSENSITIVE或TYPE_SCROLL_SENSITIVE时为了保证能游标能向上移动到任意位置，已经访问过的所有都保留在内存中不能释放。所以大量数据加载的时候，就OOM了。
     */
    public PreparedStatement createPreparedStmt(Connection conn, boolean query, String sql){
        try {
            log.debug("创建PREPARED_STMT：" + (query ? "TYPE_SCROLL_INSENSITIVE,CONCUR_READ_ONLY" : "default"));
            return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e){
            log.warn("数据库不支持此类创建的PREPARED_STMT。", e);
            log.debug("创建PREPARED_STMT2: " + (query ? "TYPE_SCROLL_SENSITIVE,CONCUR_UPDATABLE" : "default"));
            try {
                return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            } catch (Exception e1) {
                log.warn("数据库不支持此类创建的PREPARED_STMT");
                log.debug("创建PREPARED_STMT3: " + (query ? "default,default" : "default"));
                try {
                    return conn.prepareStatement(sql);
                }catch (Exception e2){
                    log.error("数据库不支持此类创建的PREPARED_STMT");
                    return null;
                }
            }
        }
    }

    public Statement createStmt(Connection conn, boolean query) {
        try {
            log.debug("创建STMT: " + (query ? "TYPE_SCROLL_INSENSITIVE,CONCUR_READ_ONLY" : "default"));

            try {
                return conn.createStatement(1004, 1007);
            } catch (Exception e) {
                log.warn("数据库不支持此类创建的STMT");
                log.debug("创建STMT2: " + (query ? "TYPE_SCROLL_SENSITIVE,CONCUR_UPDATABLE" : "default"));

                try {
                    return conn.createStatement(1005, 1008);
                } catch (Exception e1) {
                    log.warn("数据库不支持此类创建的STMT");
                    log.debug("创建STMT3: " + (query ? "default,default" : "default"));

                    try {
                        return conn.createStatement();
                    } catch (Exception e2) {
                        log.warn("数据库不支持此类创建的STMT");
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            log.error("数据库创建STMT失败。", e);
            return null;
        }
    }

}
