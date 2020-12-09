package com.ruoyi.integrator.service;

import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;

/**
 * 策略角色
 */
public interface IInForwardStrategy {

    InForwardType getType();

    Object forward(InForwardRequestVo request);

}
