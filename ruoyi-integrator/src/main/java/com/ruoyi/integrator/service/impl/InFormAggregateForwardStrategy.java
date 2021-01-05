package com.ruoyi.integrator.service.impl;

import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInAggregateForwardStrategy;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

/**
 * @description: 表单转发
 * @author: zhangxiulin
 * @time: 2020/12/8 10:16
 */
@Component
public class InFormAggregateForwardStrategy implements IInAggregateForwardStrategy {

    @Override
    public InForwardType getType() {
        return InForwardType.FORM;
    }

    @Override
    public Object forward(InForwardRequestVo request) {
        return null;
    }

    @Override
    public Future submitForward(CompletionService completionService, InForwardRequestVo request) {
        return null;
    }
}
