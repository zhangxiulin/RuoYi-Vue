package com.ruoyi.integrator.service;

import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;

import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

public interface IInAggregateForwardStrategy {

    InForwardType getType();

    Object forward(InForwardRequestVo request);

    Future submitForward(CompletionService completionService, InForwardRequestVo request);

}
