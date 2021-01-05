package com.ruoyi.integrator.service.impl;

import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInAggregateForwardStrategy;
import com.ruoyi.integrator.thread.SqlDmlForwardSendThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @description: SQL增、删、改
 * @author: zhangxiulin
 * @time: 2020/12/8 10:16
 */
@Component
public class InSqlDmlAggregateForwardStrategy implements IInAggregateForwardStrategy {

    private static final Logger log = LoggerFactory.getLogger(InSqlDmlAggregateForwardStrategy.class);


    @Override
    public InForwardType getType() {
        return InForwardType.SQL_DML;
    }

    @Override
    public Object forward(InForwardRequestVo request) {
        log.info("聚合服务子请求[" + request.getReqId() + "]为DB操纵请求");
        InForwardInfo inForwardInfo = request.getInForwardInfo();
        SqlDmlForwardSendThread sdfst = new SqlDmlForwardSendThread(inForwardInfo, request.getVar(), request.getData(), request.getDataList());
        return sdfst.call();
    }

    @Override
    public Future submitForward(CompletionService completionService, InForwardRequestVo request) {
        log.info("聚合服务子请求[" + request.getReqId() + "]为DB操纵请求，提交线程...");
        InForwardInfo inForwardInfo = request.getInForwardInfo();
        SqlDmlForwardSendThread sdfst = new SqlDmlForwardSendThread(inForwardInfo, request.getVar(), request.getData(), request.getDataList());
        return completionService.submit(sdfst);
    }


}
