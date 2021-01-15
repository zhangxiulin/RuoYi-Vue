package com.ruoyi.integrator.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.database.DatabaseAccessLayer;
import com.ruoyi.common.core.database.ops.Table;
import com.ruoyi.common.core.database.ops.TableService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.PatternUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.VelocityUtils2;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.enums.InForwardProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/8 12:42
 */
public class SqlQueryForwardSendThread implements Callable<AjaxResult> {

    private static final Logger logger = LoggerFactory.getLogger(SqlQueryForwardSendThread.class);

    private InForwardInfo inForwardInfo;

    private Map<String, Object> sendVar;

    private Map<String, Object> sendData;

    public SqlQueryForwardSendThread(InForwardInfo inForwardInfo, Map<String, Object> sendVar, Map<String, Object> sendData){
        this.inForwardInfo = inForwardInfo;
        this.sendVar = sendVar;
        this.sendData = sendData;
    }

    @Override
    public AjaxResult call() {
        AjaxResult result = null;
        logger.info("开始处理[SQL_QUERY]转发服务...");
        if (inForwardInfo != null){
            // 区分协议，目前只支持http
            if (StringUtils.isNotEmpty(inForwardInfo.getForwardProtocol())){
                InForwardProtocol forwardProtocolEnum = InForwardProtocol.valueOf(inForwardInfo.getForwardProtocol());
                switch (forwardProtocolEnum){
                    case DB:
                    {
                        logger.info("转发协议[DB] 转发编号["+inForwardInfo.getForwardCode()+"]");
                        if (StringUtils.isNotEmpty(inForwardInfo.getForwardDatasource())){
                            String datasourceName = inForwardInfo.getForwardDatasource();
                            logger.info("转发编号["+inForwardInfo.getForwardCode()+"]目标数据源[" + datasourceName + "]");
                            Connection connection = DatabaseAccessLayer.getConnection(datasourceName);
                            if (connection != null){
                                try {
                                    String forwardSql = inForwardInfo.getForwardSql();
                                    if (StringUtils.isEmpty(forwardSql)){
                                        String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]失败，SQL不能为空";
                                        logger.error(errMsg);
                                        result = AjaxResult.error(errMsg);
                                        return result;
                                    }

                                    // 替换SQL语句中的变量，占位符格式：${}
                                    if (sendVar != null) {
                                        forwardSql = VelocityUtils2.evaluate(forwardSql, sendVar);
                                    }

                                    /*// 规则： { 报文键 : SQL变量 }
                                    Map<String, Object> finalVar = new HashMap<>();
                                    String forwardVar = inForwardInfo.getForwardVar();
                                    if (forwardVar != null && !forwardVar.trim().equals("")){
                                        JSONObject forwardVarJsonObj = JSONObject.parseObject(forwardVar);
                                        if (forwardVarJsonObj != null){
                                            forwardVarJsonObj.forEach((k, v) -> {
                                                finalVar.put(String.valueOf(v), sendVar.xadatasource(k));
                                            });
                                            forwardSql = VelocityUtils2.evaluate(forwardSql, finalVar);
                                        }
                                    }*/


                                    boolean isPreparedStmt = false;
                                    String isPreparedStatement = inForwardInfo.getIsPreparedStatement();
                                    if (Constants.YES.equals(isPreparedStatement)){
                                        isPreparedStmt = true;
                                    }

                                    // 预编译STMT动态参数，格式：#{}
                                    // 规则：{ 参数名 : 参数值 }
                                    Object[] params = null;
                                    if (isPreparedStmt){
                                        List<String> matchedStrs = PatternUtils.getMatchedStrs("\\#\\{[a-zA-Z0-9_]+\\}", forwardSql);
                                        // 动态参数数组
                                        params = new Object[matchedStrs.size()];
                                        for (int i=0,len=matchedStrs.size(); i<len; i++){
                                            params[i] = sendData.get(matchedStrs.get(i).substring(2, matchedStrs.get(i).length()-1));
                                        }
                                        // 类MyBatis的语法 -> “?”占位的预编译SQL格式
                                        for (int i=0,len=matchedStrs.size(); i<len; i++){
                                            forwardSql = forwardSql.replaceAll("\\#\\{"+matchedStrs.get(i).substring(2, matchedStrs.get(i).length()-1)+"\\}", "?");
                                        }
                                    }

                                    TableService tableService = new TableService();
                                    Table queryRt = null;
                                    if (isPreparedStmt){
                                        queryRt = tableService.queryPr(connection, forwardSql, params);
                                    }else {
                                        queryRt = tableService.query(connection, forwardSql);
                                    }
                                    JSONArray jsonArray = new JSONArray();
                                    if (queryRt != null){
                                        Map<String, Integer> colMap = queryRt.getColMap();
                                        Object[][] tempResult = queryRt.getTempResult();
                                        if (tempResult != null){
                                            for (int i=0, len=tempResult.length; i<len; i++){
                                                JSONObject jsonObject = new JSONObject();
                                                Set<String> colKeySet = colMap.keySet();
                                                Iterator<String> iterator = colKeySet.iterator();
                                                while (iterator.hasNext()){
                                                    String colName = iterator.next();
                                                    jsonObject.put(colName, tempResult[i][colMap.get(colName)]);
                                                }
                                                jsonArray.add(jsonObject);
                                            }
                                        }
                                    }
                                    result = AjaxResult.success("转发编号["+inForwardInfo.getForwardCode()+"]转发成功", jsonArray.toJSONString());
                                } catch (Exception e){
                                    String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]目标数据源查询异常";
                                    logger.error(errMsg, e);
                                    result = AjaxResult.error(errMsg);
                                }
                            } else {
                                String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]失败，未找到目标数据源";
                                logger.error(errMsg);
                                result = AjaxResult.error(errMsg);
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
        logger.info("转发数据源查询服务结束.");
        return result;
    }

}
