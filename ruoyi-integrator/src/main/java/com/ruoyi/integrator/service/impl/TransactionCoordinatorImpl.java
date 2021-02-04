package com.ruoyi.integrator.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.TccTryAjaxResult;
import com.ruoyi.common.enums.HttpMethod;
import com.ruoyi.common.utils.CalendarUtils;
import com.ruoyi.common.utils.JsonUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardProtocol;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.ITransactionCoordinator;
import com.ruoyi.integrator.service.InAggregateForwardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2021/1/25 22:17
 */
@Service
public class TransactionCoordinatorImpl implements ITransactionCoordinator {

    private static final Logger log = LoggerFactory.getLogger(TransactionCoordinatorImpl.class);

    private static final String TCC_CONFIRM_REQ_PREFIX = "TCC-CONFIRM-REQ";
    private static final String TCC_CANCEL_REQ_PREFIX = "TCC-CANCEL-REQ";

    @Override
    public AjaxResult confirm(List<AjaxResult> fwdAjaxResultList)  throws Exception {
        AjaxResult ajaxResult = null;
        List<TccTryAjaxResult> tccTryAjaxResultList = new ArrayList<>();
        boolean intime = true;
        // 超时判断，以为较小的expires为准，当超过这个时间时，就不应该发送confirm确认执行的请求
        Calendar nowCalendar = Calendar.getInstance();
        for (int i=0,len=fwdAjaxResultList.size(); i<len; i++) {
            AjaxResult fwdAjaxResult = fwdAjaxResultList.get(i);
            Object dataRt = fwdAjaxResult.get(AjaxResult.DATA_TAG);
            JSONObject dataRtJsonObj = JSONObject.parseObject((String) dataRt);
            TccTryAjaxResult tccTryAjaxResult = TccTryAjaxResult.downcasting(JsonUtils.json2Map(JSON.parseObject(dataRtJsonObj.getString(AjaxResult.DATA_TAG))));
            tccTryAjaxResultList.add(tccTryAjaxResult);
            String expires = (String) tccTryAjaxResult.get(TccTryAjaxResult.EXPIRES);
            Calendar expiresCalendar = CalendarUtils.parseTimeStr(expires);
            if (nowCalendar.compareTo(expiresCalendar) > 0) {
                intime = false;
            }
        }
        // 均未超时，确认
        if (intime) {
            // 确认操作，并行
            log.info("所有参与者确认接口均未超时，进入确认阶段...");
            ExecutorService confirmExecutorService = new ThreadPoolExecutor(tccTryAjaxResultList.size(), tccTryAjaxResultList.size(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
            CompletionService confirmCompletionService = new ExecutorCompletionService(confirmExecutorService);
            for (int i=0, len=tccTryAjaxResultList.size(); i<len; i++){
                TccTryAjaxResult tccTryAjaxResult = tccTryAjaxResultList.get(i);
                String confirmUri = (String) tccTryAjaxResult.get(TccTryAjaxResult.CONFIRM_URI);
                String data = (String) tccTryAjaxResult.get(TccTryAjaxResult.DATA);
                JSONObject dataJsonObj = JSON.parseObject(data);
                String serialNumber = "";
                if (dataJsonObj.containsKey(TccTryAjaxResult.SERIAL_NUMBER)){
                    serialNumber = dataJsonObj.getString(TccTryAjaxResult.SERIAL_NUMBER);
                }
                // 构造转发实体
                InForwardInfo inForwardInfo = new InForwardInfo();
                String forwardCode = TCC_CONFIRM_REQ_PREFIX + "-" + serialNumber + "-" + i;
                inForwardInfo.setForwardCode(forwardCode);
                inForwardInfo.setForwardName("TCC确认阶段，发送确认请求");
                inForwardInfo.setIsAsync(Constants.NO);
                inForwardInfo.setForwardProtocol(InForwardProtocol.HTTP.name());
                inForwardInfo.setForwardType(InForwardType.REST.name());
                inForwardInfo.setForwardUrl(confirmUri);
                inForwardInfo.setForwardMethod(HttpMethod.PUT.name());
                InAggregateForwardContext inForwardContext = SpringUtils.getBean(InAggregateForwardContext.class);
                InForwardRequestVo inForwardRequestVo = new InForwardRequestVo();
                inForwardRequestVo.setReqId(inForwardInfo.getInfoId());
                inForwardRequestVo.setInForwardInfo(inForwardInfo);
                inForwardRequestVo.setData((Map) dataJsonObj);
                inForwardContext.submitForward(confirmCompletionService, InForwardType.REST, inForwardRequestVo);
            }

            // 确认请求结果汇总
            boolean confirmSuccess = true;
            List<AjaxResult> cfmAjaxResultList = new ArrayList<>();
            for (int i=0, len=tccTryAjaxResultList.size(); i<len; i++){
                AjaxResult confirmAjaxResult = (AjaxResult) confirmCompletionService.take().get();
                cfmAjaxResultList.add(confirmAjaxResult);
                int code = (int) confirmAjaxResult.get(AjaxResult.CODE_TAG);
                if (HttpStatus.SUCCESS != code){
                    confirmSuccess = false;
                    // 可以进行合理重试（confirm, cancel接口必须是幂等性）
                    // TODO
                }
            }

            if (confirmSuccess) {
                // 协调器会对参与者逐个发起Confirm请求, 如果一切顺利那么将会返回如下结果 HTTP/1.1 204 No Content
                String msg = "所有参与者确认成功";
                ajaxResult = new AjaxResult(HttpStatus.NO_CONTENT, msg, cfmAjaxResultList);
            } else {
                // 有些参与者确认了, 但是有些就没有. 这种情况就应该要返回409, 这种情况在Atomikos中定义为启发式异常
                String msg = "存在参与者确认失败，需要人工干预";
                ajaxResult = new AjaxResult(HttpStatus.CONFLICT, msg, cfmAjaxResultList);
            }

        } else { // 如果发起Confirm请求的时间太晚, 那么意味着所有被动方都已经进行了超时补偿
            String msg = "发起确认请求的时间太晚，参与者已经进行了超时补偿，未进入确认阶段";
            log.error(msg);
            ajaxResult = new AjaxResult(HttpStatus.NOT_FOUND, msg);
        }

        return ajaxResult;
    }

    @Override
    public AjaxResult cancel(List<AjaxResult> fwdAjaxResultList)  throws Exception {
        AjaxResult ajaxResult = null;
        List<TccTryAjaxResult> tccTryAjaxResultList = new ArrayList<>();
        for (int i=0,len=fwdAjaxResultList.size(); i<len; i++) {
            AjaxResult fwdAjaxResult = fwdAjaxResultList.get(i);
            Object dataRt = fwdAjaxResult.get(AjaxResult.DATA_TAG);
            if (dataRt != null) {
                if (dataRt instanceof String) {
                    try {
                        JSONObject dataRtJsonObj = JSONObject.parseObject((String) dataRt);
                        JSONObject sourceDataRtJsonObj = dataRtJsonObj.getJSONObject(AjaxResult.DATA_TAG);
                        if (sourceDataRtJsonObj != null) {
                            TccTryAjaxResult tccTryAjaxResult = TccTryAjaxResult.downcasting(sourceDataRtJsonObj);
                            tccTryAjaxResultList.add(tccTryAjaxResult);
                        }
                    } catch (Exception e){
                        log.info("try接口的返回值无法转为JSONObject");
                    }
                }
            }
        }
        String msg = "";
        if (tccTryAjaxResultList.size() > 0) {
            ExecutorService cancelExecutorService = new ThreadPoolExecutor(tccTryAjaxResultList.size(), tccTryAjaxResultList.size(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
            CompletionService cancelCompletionService = new ExecutorCompletionService(cancelExecutorService);
            for (int i=0, len=tccTryAjaxResultList.size(); i<len; i++){
                TccTryAjaxResult tccTryAjaxResult = tccTryAjaxResultList.get(i);
                String cancelUri = (String) tccTryAjaxResult.get(TccTryAjaxResult.CANCEL_URI);
                // 判断是否存在“取消接口”，不强求参与者提供取消接口
                if (StringUtils.isNotEmpty(cancelUri)){
                    String data = (String) tccTryAjaxResult.get(TccTryAjaxResult.DATA);
                    JSONObject dataJsonObj = JSON.parseObject(data);
                    String serialNumber = "";
                    if (dataJsonObj.containsKey(TccTryAjaxResult.SERIAL_NUMBER)){
                        serialNumber = dataJsonObj.getString(TccTryAjaxResult.SERIAL_NUMBER);
                    }
                    // 构造转发实体
                    InForwardInfo inForwardInfo = new InForwardInfo();
                    String forwardCode = TCC_CANCEL_REQ_PREFIX + "-" + serialNumber + "-" + i;
                    inForwardInfo.setForwardCode(forwardCode);
                    inForwardInfo.setForwardName("TCC取消阶段，发送取消请求");
                    inForwardInfo.setIsAsync(Constants.NO);
                    inForwardInfo.setForwardProtocol(InForwardProtocol.HTTP.name());
                    inForwardInfo.setForwardType(InForwardType.REST.name());
                    inForwardInfo.setForwardUrl(cancelUri);
                    inForwardInfo.setForwardMethod(HttpMethod.DELETE.name());
                    InAggregateForwardContext inForwardContext = SpringUtils.getBean(InAggregateForwardContext.class);
                    InForwardRequestVo inForwardRequestVo = new InForwardRequestVo();
                    inForwardRequestVo.setReqId(inForwardInfo.getInfoId());
                    inForwardRequestVo.setInForwardInfo(inForwardInfo);
                    inForwardRequestVo.setData((Map) dataJsonObj);
                    inForwardContext.submitForward(cancelCompletionService, InForwardType.REST, inForwardRequestVo);
                }
            }
            msg = "参与者取消请求提交完成";
        }else {
            msg = "取消阶段已完成，但未解析到尝试阶段的返回报文";
        }

        ajaxResult = new AjaxResult(HttpStatus.NO_CONTENT, msg);
        return ajaxResult;
    }
}
