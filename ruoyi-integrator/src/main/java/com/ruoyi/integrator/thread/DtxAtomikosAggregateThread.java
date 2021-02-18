package com.ruoyi.integrator.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.database.ops.Table;
import com.ruoyi.common.core.database.ops.TableService;
import com.ruoyi.common.core.database.ops.TableUtils;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ExecutionOrder;
import com.ruoyi.common.utils.PatternUtils;
import com.ruoyi.common.utils.RandomUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.VelocityUtils2;
import com.ruoyi.framework.datasource.jta.AtomikosDataSourceLayer;
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.enums.InForwardProtocol;
import com.ruoyi.integrator.enums.InForwardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @description: Atomikos分布式事务聚合服务转发
 *
 * @author: zhangxiulin
 * @time: 2020/12/27 22:59
 */
public class DtxAtomikosAggregateThread implements Callable<AjaxResult> {

    private static final Logger logger = LoggerFactory.getLogger(DtxAtomikosAggregateThread.class);

    private InAggregation inAggregation;

    private List sendVarList;

    private List sendDataList;

    private Map<String, Connection> cachedConn = new LinkedHashMap<>();

    public DtxAtomikosAggregateThread(InAggregation inAggregation, List sendVarList, List sendDataList){
        this.inAggregation = inAggregation;
        this.sendVarList = sendVarList;
        this.sendDataList = sendDataList;
    }

    @Override
    public AjaxResult call() throws Exception {
        logger.info("开始处理聚合服务["+inAggregation.getAggrCode()+"] 分布式事务[Atomikos]...");
        AjaxResult ajaxResult = null;
        // 获取所有被聚合的服务接口
        List<InForwardInfo> forwardInfoList = inAggregation.getForwardInfoList();
        List<AjaxResult> fwdAjaxResultList = new ArrayList<>();
        if (forwardInfoList != null && forwardInfoList.size() > 0) {
            if (ExecutionOrder.SEQUENCE.getCode().equals(inAggregation.getExecutionOrder())) { // 顺序执行

            } else {    // 并行执行

            }
            logger.info("聚合服务[" + inAggregation.getAggrCode() + "]并行执行（Atomikos方案目前只支持并行）");

            UserTransaction userTransaction = new UserTransactionImp();
            List<Connection> connList = new ArrayList<>();
            try {
                // 开启事务
                userTransaction.begin();
                for (int i = 0, len = forwardInfoList.size(); i < len; i++) {
                    InForwardInfo inForwardInfo = forwardInfoList.get(i);
                    Map<String, Object> sendVar = new HashMap<>();
                    if (sendVarList.size() == forwardInfoList.size()) {
                        sendVar = (Map<String, Object>) sendVarList.get(i);
                    }
                    Object sendData = sendDataList.get(i);
                    InForwardType forwardType = InForwardType.valueOf(inForwardInfo.getForwardType());
                    if (forwardType != null) {
                        if (InForwardType.SQL_DML == forwardType) {
                            if (StringUtils.isNotEmpty(inForwardInfo.getForwardProtocol())) {
                                InForwardProtocol forwardProtocolEnum = InForwardProtocol.valueOf(inForwardInfo.getForwardProtocol());
                                switch (forwardProtocolEnum) {
                                    case DB: {
                                        logger.info("转发协议[DB] 转发编号[" + inForwardInfo.getForwardCode() + "]");
                                        if (StringUtils.isNotEmpty(inForwardInfo.getForwardDatasource())) {
                                            String datasourceName = inForwardInfo.getForwardDatasource();
                                            logger.info("转发编号[" + inForwardInfo.getForwardCode() + "] 目标数据源[" + datasourceName + "]");
                                            Connection connection;
                                            if (cachedConn.containsKey(datasourceName)){
                                                connection = cachedConn.get(datasourceName);
                                            } else {
                                                connection = AtomikosDataSourceLayer.getConnection(datasourceName);
                                                cachedConn.put(datasourceName, connection);
                                            }
                                            String forwardSql = inForwardInfo.getForwardSql();
                                            if (StringUtils.isNotEmpty(forwardSql)) {
                                                // 替换SQL语句中的变量，占位符格式：${}
                                                if (sendVar != null) {
                                                    forwardSql = VelocityUtils2.evaluate(forwardSql, sendVar);
                                                }
                                                boolean isPreparedStmt = false;
                                                String isPreparedStatement = inForwardInfo.getIsPreparedStatement();
                                                if (Constants.YES.equals(isPreparedStatement)) {
                                                    isPreparedStmt = true;
                                                }
                                                TableService tableService = new TableService();
                                                boolean rt;
                                                if (isPreparedStmt) {
                                                    // 预编译STMT动态参数，格式：#{}
                                                    // 规则：{ 参数名 : 参数值 }
                                                    List<Object[]> list = TableUtils.prepareParamsList(forwardSql, sendData);

                                                    // 类MyBatis的语法 -> “?”占位的预编译SQL格式
                                                    forwardSql = TableUtils.prepareSql(forwardSql);

                                                    PreparedStatement preparedStmt = tableService.createPreparedStmt(connection, false, forwardSql);
                                                    for (int g = 0, leng = list.size(); g < leng; g++) {
                                                        Object[] o = list.get(g);
                                                        for (int h = 0, lenh = o.length; h < lenh; h++) {
                                                            preparedStmt.setObject(h + 1, o[h]);
                                                        }
                                                        preparedStmt.addBatch();
                                                    }
                                                    preparedStmt.executeBatch();
                                                    rt = true;
                                                } else {
                                                    Statement stmt = tableService.createStmt(connection, false);
                                                    stmt.addBatch(forwardSql);
                                                    stmt.executeBatch();
                                                    rt = true;
                                                }

                                                // 测试
                                                // if (RandomUtils.genRandomNum(2) == 1){ int s = 1/0;}

                                                if (rt) {
                                                    ajaxResult = AjaxResult.success("转发编号[" + inForwardInfo.getForwardCode() + "]转发成功");
                                                } else {
                                                    ajaxResult = AjaxResult.error("转发编号[" + inForwardInfo.getForwardCode() + "]转发失败");
                                                }
                                                fwdAjaxResultList.add(ajaxResult);

                                            } else {
                                                String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]失败，未配置SQL";
                                                logger.error(errMsg);
                                                ajaxResult = AjaxResult.error(errMsg);
                                                fwdAjaxResultList.add(ajaxResult);
                                            }
                                        }
                                    }
                                    break;
                                    default:
                                        break;
                                }
                            }
                        } else if (InForwardType.SQL_QUERY == forwardType) {
                            if (StringUtils.isNotEmpty(inForwardInfo.getForwardProtocol())) {
                                InForwardProtocol forwardProtocolEnum = InForwardProtocol.valueOf(inForwardInfo.getForwardProtocol());
                                switch (forwardProtocolEnum) {
                                    case DB: {
                                        logger.info("转发协议[DB] 转发编号[" + inForwardInfo.getForwardCode() + "]");
                                        if (StringUtils.isNotEmpty(inForwardInfo.getForwardDatasource())) {
                                            String datasourceName = inForwardInfo.getForwardDatasource();
                                            Connection connection;
                                            if (cachedConn.containsKey(datasourceName)){
                                                connection = cachedConn.get(datasourceName);
                                            } else {
                                                connection = AtomikosDataSourceLayer.getConnection(datasourceName);
                                                cachedConn.put(datasourceName, connection);
                                            }
                                            String forwardSql = inForwardInfo.getForwardSql();
                                            if (StringUtils.isNotEmpty(forwardSql)) {
                                                // 替换SQL语句中的变量，占位符格式：${}
                                                if (sendVar != null) {
                                                    forwardSql = VelocityUtils2.evaluate(forwardSql, sendVar);
                                                }
                                                boolean isPreparedStmt = false;
                                                String isPreparedStatement = inForwardInfo.getIsPreparedStatement();
                                                if (Constants.YES.equals(isPreparedStatement)) {
                                                    isPreparedStmt = true;
                                                }
                                                TableService tableService = new TableService();
                                                Table table;
                                                if (isPreparedStmt) {
                                                    // 预编译STMT动态参数，格式：#{}
                                                    // 规则：{ 参数名 : 参数值 }
                                                    Object[] params = TableUtils.prepareParams(forwardSql, sendData);

                                                    // 类MyBatis的语法 -> “?”占位的预编译SQL格式
                                                    forwardSql = TableUtils.prepareSql(forwardSql);

                                                    PreparedStatement preparedStmt = tableService.createPreparedStmt(connection, false, forwardSql);
                                                    if (params != null) {
                                                        for (int g = 0, leng = params.length; g < leng; g++) {
                                                            preparedStmt.setObject(g + 1, params[g]);
                                                        }
                                                    }

                                                    ResultSet rs = preparedStmt.executeQuery();
                                                    table = TableUtils.resultSet2Table(rs);
                                                } else {
                                                    Statement stmt = tableService.createStmt(connection, false);
                                                    ResultSet rs = stmt.executeQuery(forwardSql);
                                                    table = TableUtils.resultSet2Table(rs);
                                                }
                                                JSONArray jsonArray = TableUtils.table2Json(table);
                                                ajaxResult = AjaxResult.success("转发编号[" + inForwardInfo.getForwardCode() + "]转发成功", jsonArray.toJSONString());
                                                fwdAjaxResultList.add(ajaxResult);
                                            }
                                        }
                                    }
                                    break;
                                    default:
                                        break;
                                }
                            } else {

                            }
                        }
                    }
                }

                userTransaction.commit();
                logger.info("聚合服务["+inAggregation.getAggrCode()+"] 分布式事务[Atomikos] 提交成功");
                ajaxResult = AjaxResult.success(fwdAjaxResultList);
            }catch(Exception e){
                ajaxResult = AjaxResult.error("内部服务出现错误，最终回滚", fwdAjaxResultList);
                try {
                    logger.error("Atomikos出现异常，回滚", e);
                    userTransaction.rollback();
                } catch (SystemException se) {
                    logger.error("Atomikos回滚出现异常", se);
                }
            } finally{
                try {
                    cachedConn.forEach( (k,v) -> {
                        try {
                            v.close();
                            logger.info("Atomikos数据源["+k+"]数据库连接关闭成功");
                        } catch (SQLException e) {
                            logger.error("Atomikos关闭数据库连接出现异常", e);
                        }
                    });
                } catch (Exception ignore){

                }
            }

        }
        logger.info("聚合服务["+inAggregation.getAggrCode()+"] 分布式事务[Atomikos] 处理结束.");
        return ajaxResult;
    }
}
