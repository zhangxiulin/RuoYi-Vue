package com.ruoyi.integrator.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InAggregateRequestVo;
import com.ruoyi.integrator.domain.vo.InHttpAuthInfoVo;
import com.ruoyi.integrator.enums.DistributedTxSolution;
import com.ruoyi.integrator.mapper.InAggregationMapper;
import com.ruoyi.integrator.mapper.InForwardInfoMapper;
import com.ruoyi.integrator.service.IInAggregateService;
import com.ruoyi.integrator.service.IInAggregationService;
import com.ruoyi.integrator.service.InAggregateContext;
import com.ruoyi.integrator.thread.NoneDtxAggregateThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/27 21:15
 */
@Service
public class InAggregateServiceImpl implements IInAggregateService {

    private static final Logger log = LoggerFactory.getLogger(InAggregateServiceImpl.class);

    private static final String KEY_REQ_ID = "reqId";
    private static final String KEY_VAR = "var";
    private static final String KEY_DATA = "data";

    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private Validator validator;

    @Autowired
    private InAggregateContext inAggregateContext;

    @Autowired
    private InAggregationMapper inAggregationMapper;

    @Autowired
    private InForwardInfoMapper inForwardInfoMapper;

    @Override
    public AjaxResult aggregate(JSONObject jsonObject, InHttpAuthInfoVo inHttpAuthInfoVo) {
        AjaxResult ajaxResult = null;
        // 解析报文
        InAggregateRequestVo inAggregateRequestVo = new InAggregateRequestVo();

        // 设置当前认证信息
        inAggregateRequestVo.setInHttpAuthInfoVo(inHttpAuthInfoVo);

        if (jsonObject != null) {
            String reqId = jsonObject.getString(KEY_REQ_ID);
            inAggregateRequestVo.setReqId(reqId);
            try {
                if (jsonObject.containsKey(KEY_VAR)) {
                    inAggregateRequestVo.setVarList(jsonObject.getJSONArray(KEY_VAR));
                }
            } catch (Exception e){
                String errMsg = "聚合服务[" + reqId + "] var格式不正确";
                log.error(errMsg, e);
                ajaxResult = AjaxResult.error(errMsg);
                return ajaxResult;
            }
            try {
                if (jsonObject.containsKey(KEY_DATA)) {
                    inAggregateRequestVo.setDataList(jsonObject.getJSONArray(KEY_DATA));
                }
            } catch (Exception e){
                String errMsg = "聚合服务[" + reqId + "] data格式不正确";
                log.error(errMsg, e);
                ajaxResult = AjaxResult.error(errMsg);
                return ajaxResult;
            }

            // 初步校验报文
            Set<ConstraintViolation<InAggregateRequestVo>> constraintViolationSet = validator.validate(inAggregateRequestVo);
            if (constraintViolationSet.size() > 0){
                StringBuilder errMsgSb = new StringBuilder();
                for (ConstraintViolation<InAggregateRequestVo> v : constraintViolationSet){
                    errMsgSb.append(v.getMessage()).append("; ");
                }
                String errMsg = errMsgSb.toString();
                if (StringUtils.isNotEmpty(errMsg)) {
                    if (errMsg.length() >= 2){
                        errMsg = errMsg.substring(0, errMsg.length()-2);
                    }
                }
                if (StringUtils.isEmpty(errMsg)){
                    errMsg = "报文校验不通过";
                }
                log.error("本次请求["+reqId+"]" + errMsg);
                ajaxResult = AjaxResult.error(errMsg);
                return ajaxResult;
            }

            ajaxResult = this.aggregate(inAggregateRequestVo);

        } else {
            String errMsg = "聚合服务报文格式不正确";
            log.error(errMsg);
            ajaxResult = AjaxResult.error(errMsg);
        }
        return ajaxResult;
    }

    @Override
    public AjaxResult aggregate(InAggregateRequestVo request) {
        AjaxResult ajaxResult = null;
        InAggregation inAggregation = inAggregationMapper.selectInAggregationByCode(request.getReqId());
        if (inAggregation != null){
            request.setInAggregation(inAggregation);
            // 该聚合服务聚合的服务接口列表
            List<InForwardInfo> inForwardInfoList = inForwardInfoMapper.selectInForwardInfoListByAggrId(inAggregation.getAggrId());

            // 校验报文合法性
            if (inForwardInfoList != null) {
                int fwdAmount = inForwardInfoList.size();
                if (request.getVarList() != null) {
                    int varAmount = request.getVarList().size();
                    if (varAmount > 0) {
                        if (fwdAmount != varAmount) {
                            String errMsg = "聚合服务["+request.getReqId()+"]配置信息不正确，数组参数“var”元素个数["+varAmount+"]与子服务个数["+fwdAmount+"]不一致";
                            log.error(errMsg);
                            ajaxResult = AjaxResult.error(errMsg);
                            return ajaxResult;
                        }
                    }
                }
                if (request.getDataList() != null) {
                    int dataAmount = request.getDataList().size();
                    if (dataAmount > 0) {
                        if (fwdAmount != dataAmount) {
                            String errMsg = "聚合服务["+request.getReqId()+"]配置信息不正确，数组参数“data”元素个数["+dataAmount+"]与子服务个数["+fwdAmount+"]不一致";
                            log.error(errMsg);
                            ajaxResult = AjaxResult.error(errMsg);
                            return ajaxResult;
                        }
                    }
                }
            }

            inAggregation.setForwardInfoList(inForwardInfoList);
            // 判断是否需要分布式事务支持
            if (Constants.YES.equals(inAggregation.getIsDtx())){
                log.info("聚合服务["+request.getReqId()+"]启用了分布式事务支持");
                DistributedTxSolution dtxSolution = DistributedTxSolution.getEnumByCode(inAggregation.getDtxSolution());
                if (dtxSolution != null){
                    ajaxResult = inAggregateContext.aggregate(dtxSolution, request);
                    if (ajaxResult == null){
                        log.error("聚合服务["+request.getReqId()+"]转发器返回null");
                        ajaxResult = AjaxResult.error("聚合服务转发器返回null");
                    }
                }else{
                    log.error("聚合服务["+request.getReqId()+"]配置信息不正确，无分布式事务方案");
                    ajaxResult = AjaxResult.error("聚合服务配置信息不正确，无分布式事务方案");
                }
            } else {
                log.info("聚合服务["+request.getReqId()+"]无需分布式事务支持");
                // 判断异步
                NoneDtxAggregateThread ndat = new NoneDtxAggregateThread(inAggregation, request.getVarList(), request.getDataList());
                if (Constants.YES.equals(inAggregation.getIsAsync())){  // 异步
                    threadPoolTaskExecutor.submit(ndat);
                    ajaxResult = AjaxResult.success("聚合服务["+request.getReqId()+"]提交成功");
                } else { // 同步
                    try {
                        ajaxResult = ndat.call();
                    } catch (Exception e) {
                        log.error("聚合服务["+request.getReqId()+"]执行失败", e);
                        ajaxResult = AjaxResult.error("聚合服务执行失败");
                    }
                }
            }
        } else {
            log.error("未找到聚合服务["+request.getReqId()+"]配置信息");
            ajaxResult = AjaxResult.error("未找到聚合服务配置信息");
        }
        return ajaxResult;
    }
}
