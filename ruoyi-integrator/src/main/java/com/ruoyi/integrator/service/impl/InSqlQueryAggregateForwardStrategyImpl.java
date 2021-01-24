package com.ruoyi.integrator.service.impl;

import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInAggregateForwardStrategy;
import com.ruoyi.integrator.thread.SqlQueryForwardSendThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

/**
 * @description: SQL查询
 * @author: zhangxiulin
 * @time: 2020/12/8 10:16
 */
@Component
public class InSqlQueryAggregateForwardStrategyImpl implements IInAggregateForwardStrategy {

    private static final Logger log = LoggerFactory.getLogger(InSqlQueryAggregateForwardStrategyImpl.class);

    @Override
    public InForwardType getType() {
        return InForwardType.SQL_QUERY;
    }

    @Override
    public Object forward(InForwardRequestVo request) {
        log.info("聚合服务子请求[" + request.getReqId() + "]为DB查询请求");
        InForwardInfo inForwardInfo = request.getInForwardInfo();
        SqlQueryForwardSendThread sqfst = new SqlQueryForwardSendThread(inForwardInfo, request.getVar(), request.getData());
        return sqfst.call();
    }

    @Override
    public Future submitForward(CompletionService completionService, InForwardRequestVo request) {
        log.info("聚合服务子请求[" + request.getReqId() + "]为DB查询请求，提交线程...");
        InForwardInfo inForwardInfo = request.getInForwardInfo();
        SqlQueryForwardSendThread sqfst = new SqlQueryForwardSendThread(inForwardInfo, request.getVar(), request.getData());
        return completionService.submit(sqfst);
    }


}
