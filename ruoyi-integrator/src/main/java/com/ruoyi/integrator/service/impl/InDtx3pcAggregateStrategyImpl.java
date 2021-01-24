package com.ruoyi.integrator.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.domain.vo.InAggregateRequestVo;
import com.ruoyi.integrator.enums.DistributedTxSolution;
import com.ruoyi.integrator.service.IInAggregateStrategy;
import com.ruoyi.integrator.thread.Dtx3pcAggregateThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @description: 三阶段提交聚合转发-具体策略角色
 * @author: zhangxiulin
 * @time: 2020/12/27 21:32
 */
@Deprecated
@Component
public class InDtx3pcAggregateStrategyImpl implements IInAggregateStrategy {

    private static final Logger log = LoggerFactory.getLogger(InDtx3pcAggregateStrategyImpl.class);

    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public DistributedTxSolution getType() {
        return DistributedTxSolution.ThreePhaseCommit;
    }

    @Override
    public Object aggregate(InAggregateRequestVo request) {
        log.info("聚合服务[" + request.getReqId() + "] 分布式事务策略[3PC]");
        AjaxResult ajaxResult = null;
        // 同步、异步
        InAggregation inAggregation = request.getInAggregation();
        if (inAggregation != null){
            Dtx3pcAggregateThread d3at = new Dtx3pcAggregateThread(inAggregation, request.getVarList(), request.getDataList());
            if (Constants.YES.equals(inAggregation.getIsAsync())) {
                threadPoolTaskExecutor.submit(d3at);
                ajaxResult = AjaxResult.success("聚合服务["+request.getReqId()+"]提交成功");
            } else {
                try {
                    ajaxResult = d3at.call();
                } catch (Exception e) {
                    log.error("聚合服务["+request.getReqId()+"]执行失败", e);
                    ajaxResult = AjaxResult.error("聚合服务执行失败");
                }
            }
        }

        return ajaxResult;
    }
}
