package com.ruoyi.integrator.service.impl;

import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInForwardStrategy;
import org.springframework.stereotype.Component;

/**
 * @description: 表单转发
 * @author: zhangxiulin
 * @time: 2020/12/8 10:16
 */
@Component
public class InFormForwardStrategy implements IInForwardStrategy {

    @Override
    public InForwardType getType() {
        return InForwardType.FORM;
    }

    @Override
    public Object forward(InForwardRequestVo request) {
        return null;
    }
}
