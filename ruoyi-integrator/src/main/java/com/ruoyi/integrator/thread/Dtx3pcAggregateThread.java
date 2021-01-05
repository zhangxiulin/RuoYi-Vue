package com.ruoyi.integrator.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.jdbc.MysqlXAConnection;
import com.mysql.cj.jdbc.MysqlXid;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.database.DatabaseAccessLayer;
import com.ruoyi.common.core.database.ops.Table;
import com.ruoyi.common.core.database.ops.TableService;
import com.ruoyi.common.core.database.ops.TableUtils;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ExecutionOrder;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PatternUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.VelocityUtils2;
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.enums.InForwardProtocol;
import com.ruoyi.integrator.enums.InForwardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @description: 两阶段提交（Two Phase Commit）分布式事务聚合服务转发
 *
 *  只读断言
 *
 *     在Phase 1中，RM可以断言“我这边不涉及数据增删改”来答复TM的prepare请求，从而让这个RM脱离当前的全局事务，从而免去了Phase 2。
 *
 * 这种优化发生在其他RM都完成prepare之前的话，使用了只读断言的RM早于AP其他动作（比如说这个RM返回那些只读数据给AP）前，就释放了相关数据的上下文（比如读锁之类的），这时候其他全局事务或者本地事务就有机会去改变这些数据，结果就是无法保障整个系统的可序列化特性——通俗点说那就会有脏读的风险。
 *
 * 一阶段提交
 *
 *     如果需要增删改的数据都在同一个RM上，TM可以使用一阶段提交——跳过两阶段提交中的Phase 1，直接执行Phase 2。
 *
 * 这种优化的本质是跳过Phase 1，RM自行决定了事务分支的结果，并且在答复TM前就清除掉事务分支信息。对于这种优化的情况，TM实际上也没有必要去可靠的记录全局事务的信息，在一些异常的场景下，此时TM可能不知道事务分支的执行结果。
 *
 * @author: zhangxiulin
 * @time: 2020/12/27 22:59
 */
public class Dtx3pcAggregateThread implements Callable<AjaxResult> {

    private static final Logger logger = LoggerFactory.getLogger(Dtx3pcAggregateThread.class);

    private InAggregation inAggregation;

    private List sendVarList;

    private List sendDataList;

    public Dtx3pcAggregateThread(InAggregation inAggregation, List sendVarList, List sendDataList){
        this.inAggregation = inAggregation;
        this.sendVarList = sendVarList;
        this.sendDataList = sendDataList;
    }

    @Override
    public AjaxResult call() throws Exception {
        logger.info("开始处理聚合服务["+inAggregation.getAgrCode()+"] 分布式事务[3PC]...");
        AjaxResult ajaxResult = null;
        // 获取所有被聚合的服务接口
        List<InForwardInfo> forwardInfoList = inAggregation.getForwardInfoList();
        List<AjaxResult> fwdAjaxResultList = new ArrayList<>();
        if (forwardInfoList != null && forwardInfoList.size() > 0){
            if (ExecutionOrder.SEQUENCE.getCode().equals(inAggregation.getExecutionOrder())){ // 顺序执行

            } else {    // 并行执行

            }

            logger.info("聚合服务["+inAggregation.getAgrCode()+"]并行执行（2PC目前只支持并行）");

            // 判断是否同一个RM（datasource）
            Set<String> rmSet = new HashSet<>();

            /** XA分布式事务 **/
            boolean logXaCommands = true; // true表示打印XA语句，用于调试
            // AP请求TM执行一个分布式事务，TM生成全局事务id
            byte[] gtrid = ("g" + DateUtils.dateTimeNow()).getBytes();
            int formatId = 1;

            // RM 列表
            List<XAResource> rmList = new ArrayList<>();
            List<Xid> xidList = new ArrayList<>();

            for (int i=0,len=forwardInfoList.size(); i<len; i++){
                InForwardInfo inForwardInfo = forwardInfoList.get(i);
                Map<String, Object> sendVar = new HashMap<>();
                if (sendVarList.size() == forwardInfoList.size()){
                    sendVar = (Map<String, Object>) sendVarList.get(i);
                }
                Object sendData = sendDataList.get(i);
                InForwardType forwardType = InForwardType.getEnumByCode(inForwardInfo.getForwardType());
                if (forwardType != null){
                    // 目前只支持SQL_DML
                    if (InForwardType.SQL_DML == forwardType){
                        if (StringUtils.isNotEmpty(inForwardInfo.getForwardProtocol())){
                            InForwardProtocol forwardProtocolEnum = InForwardProtocol.valueOf(inForwardInfo.getForwardProtocol());
                            switch (forwardProtocolEnum){
                                case DB:
                                {
                                    logger.info("转发协议[DB] 转发编号["+inForwardInfo.getForwardCode()+"]");
                                    if (StringUtils.isNotEmpty(inForwardInfo.getForwardDatasource())){
                                        String datasourceName = inForwardInfo.getForwardDatasource();
                                        rmSet.add(datasourceName);
                                        logger.info("转发编号["+inForwardInfo.getForwardCode()+"] 目标数据源[" + datasourceName + "]");
                                        // 获得资源管理器操作接口实例 RM
                                        XAResource rm = null;
                                        Connection connection = null;
                                        XAConnection xaConnection = null;
                                        try {
                                            connection = DatabaseAccessLayer.getConnection(datasourceName);
                                            if (connection != null){
                                                /********************* 执行RM上的事务分支 ************************/
                                                // TODO 目前只支持MySQL

                                                // xaConnection = new MysqlXAConnection((com.mysql.cj.jdbc.JdbcConnection)connection, logXaCommands);
                                                //
                                                // 以上代码会报错：HikariProxyConnection cannot be cast to com.mysql.cj.jdbc.JdbcConnection
                                                // 原因：连接池通常包装了一个真实的真实的Connection实例
                                                // 解决方案：增加此代码，将Connection转换为XXXConnection
                                                //
                                                xaConnection = new MysqlXAConnection(connection.unwrap(com.mysql.cj.jdbc.JdbcConnection.class), logXaCommands);
                                                rm = xaConnection.getXAResource();
                                                // TM生成RM上的事务分支id
                                                byte[] bqual = ("b" + i).getBytes();
                                                Xid xid = new MysqlXid(gtrid, bqual, formatId);
                                                // 执行 RM 上的事务分支
                                                rm.start(xid, XAResource.TMNOFLAGS);
                                                String forwardSql = inForwardInfo.getForwardSql();
                                                if (StringUtils.isNotEmpty(forwardSql)){
                                                    // 替换SQL语句中的变量，占位符格式：${}
                                                    if (sendVar != null) {
                                                        forwardSql = VelocityUtils2.evaluate(forwardSql, sendVar);
                                                    }
                                                    boolean isPreparedStmt = false;
                                                    String isPreparedStatement = inForwardInfo.getIsPreparedStatement();
                                                    if (Constants.YES.equals(isPreparedStatement)){
                                                        isPreparedStmt = true;
                                                    }
                                                    TableService tableService = new TableService();
                                                    boolean rt;
                                                    if (isPreparedStmt){
                                                        // 预编译STMT动态参数，格式：#{}
                                                        // 规则：{ 参数名 : 参数值 }
                                                        List<Object[]> list = new ArrayList<>(1);
                                                        List<String> matchedStrs = PatternUtils.getMatchedStrs("\\#\\{[a-zA-Z0-9_]+\\}", forwardSql);
                                                        // 判断单笔批量
                                                        if (sendData != null){
                                                            if (sendData instanceof List){
                                                                ((List)sendData).forEach( d -> {  // 批量
                                                                    Object[] params = null;
                                                                    // 动态参数数组
                                                                    params = new Object[matchedStrs.size()];
                                                                    for (int j=0,lenj=matchedStrs.size(); j<lenj; j++){
                                                                        params[j] = ((Map)d).get(matchedStrs.get(j).substring(2, matchedStrs.get(j).length()-1));
                                                                    }
                                                                    list.add(params);
                                                                });
                                                            } else {
                                                                Object[] params = null;
                                                                // 动态参数数组
                                                                params = new Object[matchedStrs.size()];
                                                                for (int j=0,lenj=matchedStrs.size(); j<lenj; j++){
                                                                    params[j] = ((Map)sendData).get(matchedStrs.get(j).substring(2, matchedStrs.get(j).length()-1));
                                                                }
                                                                list.add(params);
                                                            }
                                                        }
                                                        // 类MyBatis的语法 -> “?”占位的预编译SQL格式
                                                        for (int j=0,lenj=matchedStrs.size(); j<lenj; j++){
                                                            forwardSql = forwardSql.replaceAll("\\#\\{"+matchedStrs.get(j).substring(2, matchedStrs.get(j).length()-1)+"\\}", "?");
                                                        }
                                                        //rt = tableService.updateTxPr(connection, forwardSql, list);
                                                        PreparedStatement preparedStmt = tableService.createPreparedStmt(connection, false, forwardSql);
                                                        for (int g=0,leng=list.size(); g<leng; g++){
                                                            Object[] o = list.get(g);
                                                            for (int h=0,lenh=o.length; h<lenh; h++){
                                                                preparedStmt.setObject(h+1, o[h]);
                                                            }
                                                            preparedStmt.addBatch();
                                                        }
                                                        preparedStmt.executeBatch();
                                                        rt = true;
                                                    } else {
                                                        //rt = tableService.updateTx(connection, forwardSql);
                                                        Statement stmt = tableService.createStmt(connection, false);
                                                        stmt.addBatch(forwardSql);
                                                        stmt.executeBatch();
                                                        rt = true;
                                                    }

                                                    if (rt){
                                                        ajaxResult = AjaxResult.success("转发编号[" + inForwardInfo.getForwardCode() +"]转发成功");
                                                    } else {
                                                        ajaxResult = AjaxResult.success("转发编号[" + inForwardInfo.getForwardCode() +"]转发失败");
                                                    }
                                                    fwdAjaxResultList.add(ajaxResult);

                                                    rm.end(xid, XAResource.TMSUCCESS);
                                                    rmList.add(rm);
                                                    xidList.add(xid);
                                                }
                                            } else {
                                                String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]失败";
                                                logger.error(errMsg);
                                                ajaxResult = AjaxResult.error(errMsg);
                                                fwdAjaxResultList.add(ajaxResult);
                                            }

                                        }catch (Exception e){
                                            String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]目标数据源更新异常";
                                            logger.error(errMsg, e);
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
                    } if (InForwardType.SQL_QUERY == forwardType) {
                        /** 为了避免脏读，只读SQL没有优化2阶段提交 **/
                        if (StringUtils.isNotEmpty(inForwardInfo.getForwardProtocol())){
                            InForwardProtocol forwardProtocolEnum = InForwardProtocol.valueOf(inForwardInfo.getForwardProtocol());
                            switch (forwardProtocolEnum){
                                case DB:
                                {
                                    logger.info("转发协议[DB] 转发编号["+inForwardInfo.getForwardCode()+"]");
                                    if (StringUtils.isNotEmpty(inForwardInfo.getForwardDatasource())){
                                        String datasourceName = inForwardInfo.getForwardDatasource();
                                        rmSet.add(datasourceName);
                                        logger.info("转发编号["+inForwardInfo.getForwardCode()+"] 目标数据源[" + datasourceName + "]");
                                        // 获得资源管理器操作接口实例 RM
                                        XAResource rm = null;
                                        Connection connection = null;
                                        XAConnection xaConnection = null;
                                        try {
                                            connection = DatabaseAccessLayer.getConnection(datasourceName);
                                            if (connection != null){
                                                /********************* 执行RM上的事务分支 ************************/
                                                // TODO 目前只支持MySQL

                                                // xaConnection = new MysqlXAConnection((com.mysql.cj.jdbc.JdbcConnection)connection, logXaCommands);
                                                //
                                                // 以上代码会报错：HikariProxyConnection cannot be cast to com.mysql.cj.jdbc.JdbcConnection
                                                // 原因：连接池通常包装了一个真实的真实的Connection实例
                                                // 解决方案：增加此代码，将Connection转换为XXXConnection
                                                //
                                                xaConnection = new MysqlXAConnection(connection.unwrap(com.mysql.cj.jdbc.JdbcConnection.class), logXaCommands);
                                                rm = xaConnection.getXAResource();
                                                // TM生成RM上的事务分支id
                                                byte[] bqual = ("b" + i).getBytes();
                                                Xid xid = new MysqlXid(gtrid, bqual, formatId);
                                                // 执行 RM 上的事务分支
                                                rm.start(xid, XAResource.TMNOFLAGS);
                                                String forwardSql = inForwardInfo.getForwardSql();
                                                if (StringUtils.isNotEmpty(forwardSql)){
                                                    // 替换SQL语句中的变量，占位符格式：${}
                                                    if (sendVar != null) {
                                                        forwardSql = VelocityUtils2.evaluate(forwardSql, sendVar);
                                                    }
                                                    boolean isPreparedStmt = false;
                                                    String isPreparedStatement = inForwardInfo.getIsPreparedStatement();
                                                    if (Constants.YES.equals(isPreparedStatement)){
                                                        isPreparedStmt = true;
                                                    }
                                                    TableService tableService = new TableService();
                                                    Table table = new Table();
                                                    boolean rt;
                                                    if (isPreparedStmt){
                                                        // 预编译STMT动态参数，格式：#{}
                                                        // 规则：{ 参数名 : 参数值 }
                                                        List<String> matchedStrs = PatternUtils.getMatchedStrs("\\#\\{[a-zA-Z0-9_]+\\}", forwardSql);

                                                        Object[] params = null;
                                                        // 动态参数数组
                                                        params = new Object[matchedStrs.size()];
                                                        for (int j=0,lenj=matchedStrs.size(); j<lenj; j++){
                                                            params[j] = ((Map)sendData).get(matchedStrs.get(j).substring(2, matchedStrs.get(j).length()-1));
                                                        }

                                                        // 类MyBatis的语法 -> “?”占位的预编译SQL格式
                                                        for (int j=0,lenj=matchedStrs.size(); j<lenj; j++){
                                                            forwardSql = forwardSql.replaceAll("\\#\\{"+matchedStrs.get(j).substring(2, matchedStrs.get(j).length()-1)+"\\}", "?");
                                                        }

                                                        PreparedStatement preparedStmt = tableService.createPreparedStmt(connection, false, forwardSql);
                                                        if (params != null){
                                                            for (int g=0,leng=params.length; g<leng; g++){
                                                                preparedStmt.setObject(g+1, params[g]);
                                                            }
                                                        }

                                                        ResultSet rs =  preparedStmt.executeQuery();
                                                        if (rs != null) {
                                                            table = table.addColumns(TableUtils.mapColumns(rs, new HashMap()));
                                                            Object[][] r = TableUtils.mapValues(rs, table.getColMap().size());
                                                            if (r != null && r.length > 0) {
                                                                table = table.addData(r);
                                                            }
                                                        }
                                                    } else {
                                                        Statement stmt = tableService.createStmt(connection, false);
                                                        ResultSet rs = stmt.executeQuery(forwardSql);
                                                        if (rs != null) {
                                                            table = table.addColumns(TableUtils.mapColumns(rs, new HashMap()));
                                                            Object[][] r = TableUtils.mapValues(rs, table.getColMap().size());
                                                            if (r != null && r.length > 0) {
                                                                table = table.addData(r);
                                                            }
                                                        }
                                                    }

                                                    JSONArray jsonArray = new JSONArray();
                                                    if (table != null){
                                                        Map<String, Integer> colMap = table.getColMap();
                                                        Object[][] tempResult = table.getTempResult();
                                                        if (tempResult != null){
                                                            for (int g=0, leng=tempResult.length; g<leng; g++){
                                                                JSONObject jsonObject = new JSONObject();
                                                                Set<String> colKeySet = colMap.keySet();
                                                                Iterator<String> iterator = colKeySet.iterator();
                                                                while (iterator.hasNext()){
                                                                    String colName = iterator.next();
                                                                    jsonObject.put(colName, tempResult[g][colMap.get(colName)]);
                                                                }
                                                                jsonArray.add(jsonObject);
                                                            }
                                                        }
                                                    }
                                                    ajaxResult = AjaxResult.success("转发编号["+inForwardInfo.getForwardCode()+"]转发成功", jsonArray.toJSONString());
                                                    fwdAjaxResultList.add(ajaxResult);

                                                    rm.end(xid, XAResource.TMSUCCESS);
                                                    rmList.add(rm);
                                                    xidList.add(xid);
                                                }
                                            } else {
                                                String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]失败";
                                                logger.error(errMsg);
                                                ajaxResult = AjaxResult.error(errMsg);
                                                fwdAjaxResultList.add(ajaxResult);
                                            }

                                        }catch (Exception e){
                                            String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]目标数据源更新异常";
                                            logger.error(errMsg, e);
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
                    } else {
                        // TODO
                    }
                }
            }

            /********************* 两阶段提交 ************************/
            // phase1: 询问所有的RM准备提交事务分支
            List<Integer> rmPrepareList = new ArrayList<>();
            for (int i=0,len=rmList.size(); i<len; i++) {
                rmPrepareList.add(rmList.get(i).prepare(xidList.get(i)));
            }
            // phase2: 提交所有事务分支
            boolean onePhase = false;
            if (rmSet.size() == 1){  // 如果需要增删改的数据都在同一个RM上，TM可以使用一阶段提交
                onePhase = true;
            }

            // 所有事务分支都prepare成功，提交所有事务分支
            boolean allSuccess = true;
            for (int i=0,len=rmPrepareList.size(); i<len; i++) {
                if (XAResource.XA_OK != rmPrepareList.get(i)) {
                    allSuccess = false;
                    break;
                }
            }
            if (allSuccess) {
                for (int i=0,len=rmList.size(); i<len; i++){
                    rmList.get(i).commit(xidList.get(i), onePhase);
                }
                ajaxResult = AjaxResult.success(fwdAjaxResultList);
            } else {
                for (int i=0,len=rmList.size(); i<len; i++){
                    rmList.get(i).rollback(xidList.get(i));
                }
                ajaxResult = AjaxResult.error("内部服务出现错误", fwdAjaxResultList);
            }
        }
        logger.info("聚合服务["+inAggregation.getAgrCode()+"] 分布式事务[3PC] 处理结束.");
        return ajaxResult;
    }
}
