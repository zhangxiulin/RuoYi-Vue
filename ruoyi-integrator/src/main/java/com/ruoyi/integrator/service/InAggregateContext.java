package com.ruoyi.integrator.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.integrator.domain.vo.InAggregateRequestVo;
import com.ruoyi.integrator.enums.DistributedTxSolution;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 环境角色
 * @author: zhangxiulin
 * @time: 2020/12/27 21:36
 */
@Component
public class InAggregateContext {

    Map<DistributedTxSolution, IInAggregateStrategy> aggregateStrategyMap = new HashMap<>();

    public InAggregateContext(List<IInAggregateStrategy> aggregateStrategyList){
        for (IInAggregateStrategy aggregateStrategy : aggregateStrategyList){
            aggregateStrategyMap.put(aggregateStrategy.getType(), aggregateStrategy);
        }
    }

    public AjaxResult aggregate(DistributedTxSolution distributedTxSolution, InAggregateRequestVo request){
        IInAggregateStrategy aggregateStrategy = aggregateStrategyMap.get(distributedTxSolution);
        if (aggregateStrategy != null){
            return (AjaxResult) aggregateStrategy.aggregate(request);
        } else {
            throw new CustomException("未找到类型[" + distributedTxSolution + "]分布式事务处理器");
        }
    }

}
