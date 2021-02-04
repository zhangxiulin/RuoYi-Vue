package com.ruoyi.integrator.thread;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ExecutionOrder;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.InAggregateForwardContext;
import com.ruoyi.integrator.service.InForwardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @description: 无分布式事务聚合服务转发
 * @author: zhangxiulin
 * @time: 2020/12/27 22:59
 */
public class NoneDtxAggregateThread  implements Callable<AjaxResult> {

    private static final Logger logger = LoggerFactory.getLogger(NoneDtxAggregateThread.class);

    private InAggregation inAggregation;

    private List sendVarList;

    private List sendDataList;

    public NoneDtxAggregateThread(InAggregation inAggregation, List sendVarList, List sendDataList){
        this.inAggregation = inAggregation;
        this.sendVarList = sendVarList;
        this.sendDataList = sendDataList;
    }

    @Override
    public AjaxResult call() throws Exception {
        logger.info("开始处理聚合服务["+inAggregation.getAgrCode()+"] 分布式事务[无]...");
        AjaxResult ajaxResult = null;
        // 获取所有被聚合的服务接口
        List<InForwardInfo> forwardInfoList = inAggregation.getForwardInfoList();
        List<AjaxResult> fwdAjaxResultList = new ArrayList<>();
        if (forwardInfoList != null && forwardInfoList.size() > 0){
            if (ExecutionOrder.SEQUENCE.getCode().equals(inAggregation.getExecutionOrder())){ // 顺序执行
                logger.info("聚合服务["+inAggregation.getAgrCode()+"]顺序执行");
                for (int i=0,len=forwardInfoList.size(); i<len; i++){
                    InForwardInfo inForwardInfo = forwardInfoList.get(i);
                    Object sendVar = sendVarList.get(i);
                    Object sendData = sendDataList.get(i);
                    InForwardRequestVo inForwardRequestVo = new InForwardRequestVo();
                    inForwardRequestVo.setReqId(inForwardInfo.getForwardCode());
                    inForwardRequestVo.setInForwardInfo(inForwardInfo);
                    inForwardRequestVo.setVar((Map) sendVar);
                    if (sendData instanceof List){
                        inForwardRequestVo.setDataList((List)sendData);
                    } else {
                        inForwardRequestVo.setData((Map) sendData);
                    }
                    InAggregateForwardContext inForwardContext = SpringUtils.getBean(InAggregateForwardContext.class);
                    InForwardType forwardType = InForwardType.valueOf(inForwardInfo.getForwardType());
                    if (forwardType != null){
                        AjaxResult fwdAjaxResult = inForwardContext.forward(forwardType, inForwardRequestVo);
                        fwdAjaxResultList.add(fwdAjaxResult);
                    }
                }
                // 判断总结果
                boolean rtAgr = true;
                for (int i=0,len=fwdAjaxResultList.size(); i<len; i++){
                    AjaxResult fwdAjaxResult = fwdAjaxResultList.get(i);
                    int code = (int) fwdAjaxResult.get(AjaxResult.CODE_TAG);
                    if (HttpStatus.SUCCESS != code){
                        rtAgr = false;
                    }
                }
                if (rtAgr){
                    ajaxResult = AjaxResult.success(fwdAjaxResultList);
                } else {
                    ajaxResult = AjaxResult.error("内部服务出现错误", fwdAjaxResultList);
                }
            } else {    // 并行执行
                logger.info("聚合服务["+inAggregation.getAgrCode()+"]并行执行");
                int forwardSize = forwardInfoList.size();
                ExecutorService executorService = new ThreadPoolExecutor(forwardSize, forwardSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
                CompletionService completionService = new ExecutorCompletionService(executorService);
                for (int i=0; i<forwardSize; i++){
                    InForwardInfo inForwardInfo = forwardInfoList.get(i);
                    Object sendVar = new HashMap<>();
                    if (sendVarList.size() == forwardSize){
                        sendVar = sendVarList.get(i);
                    }
                    Object sendData = sendDataList.get(i);
                    InForwardRequestVo inForwardRequestVo = new InForwardRequestVo();
                    inForwardRequestVo.setReqId(inForwardInfo.getForwardCode());
                    inForwardRequestVo.setInForwardInfo(inForwardInfo);
                    inForwardRequestVo.setVar((Map) sendVar);
                    if (sendData instanceof List){
                        inForwardRequestVo.setDataList((List)sendData);
                    } else {
                        inForwardRequestVo.setData((Map) sendData);
                    }
                    InAggregateForwardContext inForwardContext = SpringUtils.getBean(InAggregateForwardContext.class);
                    InForwardType forwardType = InForwardType.valueOf(inForwardInfo.getForwardType());
                    if (forwardType != null){
                        inForwardContext.submitForward(completionService, forwardType, inForwardRequestVo);
                    }
                }
                // 汇总结果
                boolean rtAgr = true;
                for (int i=0; i<forwardSize; i++){
                    AjaxResult fwdAjaxResult = (AjaxResult) completionService.take().get();
                    int code = (int) fwdAjaxResult.get(AjaxResult.CODE_TAG);
                    if (HttpStatus.SUCCESS != code){
                        rtAgr = false;
                    }
                    fwdAjaxResultList.add(fwdAjaxResult);
                }
                if (rtAgr){
                    ajaxResult = AjaxResult.success(fwdAjaxResultList);
                } else {
                    ajaxResult = AjaxResult.error("内部服务出现错误", fwdAjaxResultList);
                }
            }

        }
        logger.info("聚合服务["+inAggregation.getAgrCode()+"] 分布式事务[无] 处理结束.");
        return ajaxResult;
    }
}
