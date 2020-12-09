package com.ruoyi.integrator.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInForwardService;
import com.ruoyi.integrator.service.IInForwardStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 转发策略Context上下文角色
 * @author: zhangxiulin
 * @time: 2020/11/27 13:28
 */
@Service
public class InForwardServiceImpl implements IInForwardService {

    Map<InForwardType, IInForwardStrategy> forwardStrategyMap = new HashMap<>();

    // 构造函数，如果是集合接口对象，那么SpringBoot就会把Spring容器中所有关于该接口的子类，全部放入到集合中
    public InForwardServiceImpl(List<IInForwardStrategy> forwardStrategys){
        for (IInForwardStrategy forwardStrategy : forwardStrategys) {
            forwardStrategyMap.put(forwardStrategy.getType(), forwardStrategy);
        }
    }

    @Override
    public AjaxResult forward(InForwardType type, InForwardRequestVo request  ) {
        IInForwardStrategy iInForwardStrategy = forwardStrategyMap.get(type);
        if (iInForwardStrategy != null){
            return (AjaxResult) iInForwardStrategy.forward(request);
        }else {
            throw new CustomException("未找到类型[" + type + "]转发处理器");
        }
    }
}
