package com.ruoyi.integrator.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.TccTryAjaxResult;
import com.ruoyi.common.enums.ExecutionOrder;
import com.ruoyi.common.enums.HttpMethod;
import com.ruoyi.common.enums.TccStage;
import com.ruoyi.common.utils.CalendarUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardProtocol;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.ITransactionCoordinator;
import com.ruoyi.integrator.service.InAggregateForwardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * @description: Try-Confirm-Cancel 分布式事务聚合服务转发
 *
 * @author: zhangxiulin
 * @time: 2020/12/27 22:59
 */
public class DtxTccAggregateThread implements Callable<AjaxResult> {

    private static final Logger logger = LoggerFactory.getLogger(DtxTccAggregateThread.class);

    private static final String SUCCESS_CODE_PRE = "2";

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
        // 获取所有被聚合的服务接口
        List<InForwardInfo> forwardInfoList = inAggregation.getForwardInfoList();
        List<AjaxResult> fwdAjaxResultList = new ArrayList<>();
        if (forwardInfoList != null && forwardInfoList.size() > 0) {
            if (ExecutionOrder.SEQUENCE.getCode().equals(inAggregation.getExecutionOrder())) { // 顺序执行
                logger.info("聚合服务["+inAggregation.getAgrCode()+"]顺序执行");
                for (int i=0,len=forwardInfoList.size(); i<len; i++){
                    InForwardInfo inForwardInfo = forwardInfoList.get(i);
                    Map<String, Object> sendVar = new HashMap<>();
                    if (sendVarList != null && sendVarList.size() == forwardInfoList.size()) {
                        sendVar = (Map<String, Object>) sendVarList.get(i);
                    }
                    Object sendData = sendDataList.get(i);
                    InForwardRequestVo inForwardRequestVo = new InForwardRequestVo();
                    inForwardRequestVo.setReqId(inForwardInfo.getForwardCode());
                    inForwardRequestVo.setInForwardInfo(inForwardInfo);
                    inForwardRequestVo.setVar(sendVar);
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

            } else {    // 并行执行

            }
            // 判断总结果，全部try成功，进行确认操作；否则进行取消操作
            boolean rtAgr = true;
            for (int i=0,len=fwdAjaxResultList.size(); i<len; i++){
                AjaxResult fwdAjaxResult = fwdAjaxResultList.get(i);
                int code = (int) fwdAjaxResult.get(AjaxResult.CODE_TAG);
                if (HttpStatus.SUCCESS != code){
                    rtAgr = false;
                    break;
                }
                // 判断内层结果
                Object sourceData = fwdAjaxResult.get(AjaxResult.DATA_TAG);
                if (sourceData != null) {
                    if (sourceData instanceof String) {
                        try {
                            JSONObject sourceDataJsonObj = JSONObject.parseObject((String) sourceData);
                            if (sourceDataJsonObj.containsKey(AjaxResult.CODE_TAG)) {
                                // 判断返回码是否是2XX
                                if (!sourceDataJsonObj.getString(AjaxResult.CODE_TAG).startsWith(SUCCESS_CODE_PRE)) {
                                    rtAgr = false;
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            logger.info("转发目标接口的返回值无法转为JSONObject");
                        }
                    }
                }
            }
            // 确认
            if (rtAgr) {
                String msg = "尝试阶段参与者全部成功，进行确认...";
                logger.info(msg, fwdAjaxResultList);
                ITransactionCoordinator transactionCoordinator = SpringUtils.getBean(ITransactionCoordinator.class);
                AjaxResult confirmedAjaxResult = transactionCoordinator.confirm(fwdAjaxResultList);
                Map<String, Object> tccStageRt = new HashMap<>();
                tccStageRt.put(TccStage.TRIED.name().toLowerCase(), fwdAjaxResultList);
                tccStageRt.put(TccStage.CONFIRMED.name().toLowerCase(), confirmedAjaxResult);
                if (confirmedAjaxResult != null) {
                    switch ((int)confirmedAjaxResult.get(AjaxResult.CODE_TAG)){
                        case HttpStatus.NO_CONTENT: {
                            ajaxResult = AjaxResult.success("所有参与者确认成功", tccStageRt);
                        }
                        break;
                        case HttpStatus.NOT_FOUND: {
                            ajaxResult = AjaxResult.error("发起确认请求的时间太晚，参与者已经进行了超时补偿", tccStageRt);
                        }
                        break;
                        case HttpStatus.CONFLICT: {
                            ajaxResult = AjaxResult.error("部分参与者确认了，部分没有，需要人工干预", tccStageRt);
                        }
                        break;
                        default:
                            break;
                    }
                }
            } else { // try失败，直接返回
                String msg = "尝试阶段失败，进行取消";
                logger.error(msg, fwdAjaxResultList);
                ITransactionCoordinator transactionCoordinator = SpringUtils.getBean(ITransactionCoordinator.class);
                AjaxResult canceledAjaxResult = transactionCoordinator.cancel(fwdAjaxResultList);
                Map<String, Object> tccStageRt = new HashMap<>();
                tccStageRt.put(TccStage.TRIED.name().toLowerCase(), fwdAjaxResultList);
                tccStageRt.put(TccStage.CANCELED.name().toLowerCase(), canceledAjaxResult);
                if (canceledAjaxResult != null) {
                    switch ((int)canceledAjaxResult.get(AjaxResult.CODE_TAG)){
                        case HttpStatus.NO_CONTENT: {
                            ajaxResult = AjaxResult.error("尝试阶段失败，取消请求已触发", tccStageRt);
                        }
                        break;
                        default:
                            break;
                    }
                } else {
                    ajaxResult = AjaxResult.error("尝试阶段失败", tccStageRt);
                }
            }
        }
        logger.info("聚合服务["+inAggregation.getAgrCode()+"] 分布式事务[TCC] 处理结束.");
        return ajaxResult;
    }
}
