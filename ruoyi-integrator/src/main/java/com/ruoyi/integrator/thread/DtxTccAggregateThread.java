package com.ruoyi.integrator.thread;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.InAggregation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @description: Try-Confirm-Cancel 分布式事务聚合服务转发
 *
 * @author: zhangxiulin
 * @time: 2020/12/27 22:59
 */
public class DtxTccAggregateThread implements Callable<AjaxResult> {

    private static final Logger logger = LoggerFactory.getLogger(DtxTccAggregateThread.class);

    private InAggregation inAggregation;

    private List sendVarList;

    private List sendDataList;

    public DtxTccAggregateThread(InAggregation inAggregation, List sendVarList, List sendDataList){
        this.inAggregation = inAggregation;
        this.sendVarList = sendVarList;
        this.sendDataList = sendDataList;
    }

    @Override
    public AjaxResult call() throws Exception {
        logger.info("开始处理聚合服务["+inAggregation.getAgrCode()+"] 分布式事务[TCC]...");
        AjaxResult ajaxResult = null;

        logger.info("聚合服务["+inAggregation.getAgrCode()+"] 分布式事务[TCC] 处理结束.");
        return ajaxResult;
    }
}
