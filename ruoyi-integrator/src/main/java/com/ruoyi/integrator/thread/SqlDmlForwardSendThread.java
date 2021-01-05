package com.ruoyi.integrator.thread;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.database.DatabaseAccessLayer;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/8 12:42
 */
public class SqlDmlForwardSendThread implements Callable<AjaxResult> {

    private static final Logger logger = LoggerFactory.getLogger(SqlDmlForwardSendThread.class);

    private InForwardInfo inForwardInfo;

    private Map<String, Object> sendVar;

    private Map<String, Object> sendData;

    private List<Map> sendDataList;

    public SqlDmlForwardSendThread(InForwardInfo inForwardInfo, Map<String, Object> sendVar, Map<String, Object> sendData, List<Map> sendDataList){
        this.inForwardInfo = inForwardInfo;
        this.sendVar = sendVar;
        this.sendData = sendData;
        this.sendDataList = sendDataList;
    }

    @Override
    public AjaxResult call() {
        AjaxResult result = null;
        logger.info("开始处理[SQL_DML]转发服务...");
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
                            logger.info("转发编号["+inForwardInfo.getForwardCode()+"] 目标数据源[" + datasourceName + "]");
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
                                    forwardSql = VelocityUtils2.evaluate(forwardSql, sendVar);

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
                                        if (sendDataList != null){ // 批量
                                            sendDataList.forEach( d -> {
                                                Object[] params = null;
                                                // 动态参数数组
                                                params = new Object[matchedStrs.size()];
                                                for (int i=0,len=matchedStrs.size(); i<len; i++){
                                                    params[i] = d.get(matchedStrs.get(i).substring(2, matchedStrs.get(i).length()-1));
                                                }
                                                list.add(params);
                                            });
                                        } else { // 单笔
                                            Object[] params = null;
                                            // 动态参数数组
                                            params = new Object[matchedStrs.size()];
                                            for (int i=0,len=matchedStrs.size(); i<len; i++){
                                                params[i] = sendData.get(matchedStrs.get(i).substring(2, matchedStrs.get(i).length()-1));
                                            }
                                            list.add(params);
                                        }

                                        // 类MyBatis的语法 -> “?”占位的预编译SQL格式
                                        for (int i=0,len=matchedStrs.size(); i<len; i++){
                                            forwardSql = forwardSql.replaceAll("\\#\\{"+matchedStrs.get(i).substring(2, matchedStrs.get(i).length()-1)+"\\}", "?");
                                        }

                                        rt = tableService.updateTxPr(connection, forwardSql, list);
                                    } else {
                                        rt = tableService.updateTx(connection, forwardSql);
                                    }

                                    if (rt){
                                        result = AjaxResult.success("转发编号[" + inForwardInfo.getForwardCode() +"]转发成功");
                                    } else {
                                        result = AjaxResult.success("转发编号[" + inForwardInfo.getForwardCode() +"]转发失败");
                                    }

                                } catch (Exception e){
                                    String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]目标数据源更新异常";
                                    logger.error(errMsg, e);
                                    result = AjaxResult.error(errMsg);
                                }
                            } else {
                                String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]失败";
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
        logger.info("转发数据源操纵服务结束.");
        return result;
    }

}
