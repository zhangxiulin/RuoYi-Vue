package com.ruoyi.integrator.service;

import com.ruoyi.integrator.domain.vo.InAggregateRequestVo;
import com.ruoyi.integrator.enums.DistributedTxSolution;

/**
 * @description: 聚合服务转发-策略角色
 * @author: zhangxiulin
 * @time: 2020/12/27 21:25
 */
public interface IInAggregateStrategy {

    DistributedTxSolution getType();

    Object aggregate(InAggregateRequestVo request);

}
