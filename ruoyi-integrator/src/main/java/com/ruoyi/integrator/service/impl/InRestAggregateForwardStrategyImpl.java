package com.ruoyi.integrator.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInAggregateForwardStrategy;
import com.ruoyi.integrator.thread.RestForwardSendThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

/**
 * @description: 转发策略Rest实现
 * @author: zhangxiulin
 * @time: 2020/12/8 10:16
 */
@Component
public class InRestAggregateForwardStrategyImpl implements IInAggregateForwardStrategy {

    private static final Logger logger = LoggerFactory.getLogger(InRestAggregateForwardStrategyImpl.class);

    @Override
    public InForwardType getType() {
        return InForwardType.REST;
    }

    @Override
    public Object forward(InForwardRequestVo request) {
        logger.info("聚合服务子请求[" + request.getReqId() + "]为HTTP转发请求");
        InForwardInfo inForwardInfo = request.getInForwardInfo();
        RestForwardSendThread rfst = new RestForwardSendThread(inForwardInfo, request.getVar(), request.getData());
        return rfst.call();
    }

    @Override
    public Future<AjaxResult> submitForward(CompletionService completionService, InForwardRequestVo request){
        logger.info("聚合服务子请求[" + request.getReqId() + "]为HTTP转发请求，提交线程...");
        InForwardInfo inForwardInfo = request.getInForwardInfo();
        RestForwardSendThread rfst = new RestForwardSendThread(inForwardInfo, request.getVar(), request.getData());
        return completionService.submit(rfst);
    }


}
