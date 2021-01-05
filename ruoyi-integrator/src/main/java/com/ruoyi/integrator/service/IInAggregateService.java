package com.ruoyi.integrator.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.vo.InAggregateRequestVo;

/**
 * @description: 聚合服务
 * @author: zhangxiulin
 * @time: 2020/12/27 21:12
 */
public interface IInAggregateService {

    AjaxResult aggregate(InAggregateRequestVo request);

}
