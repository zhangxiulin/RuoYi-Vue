package com.ruoyi.integrator.controller;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/27 17:35
 */

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.domain.vo.InAggregateRequestVo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.service.IInAggregateService;
import com.ruoyi.integrator.service.IInAggregationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @description: 聚合服务转发Controller
 * @author: zhangxiulin
 * @time: 2020/11/27 12:55
 */
@RestController
@RequestMapping("/integrator")
public class InAggregateController {

    private static final Logger log = LoggerFactory.getLogger(InAggregateController.class);

    private static final String KEY_REQ_ID = "reqId";
    private static final String KEY_VAR = "var";
    private static final String KEY_DATA = "data";

    @Autowired
    private IInAggregationService inAggregationService;

    @Autowired
    private IInAggregateService inAggregateService;

    @Autowired
    private Validator validator;

    @PostMapping(value = "/aggr")
    public AjaxResult aggregate(@RequestBody JSONObject jsonObject) {
        AjaxResult ajaxResult = null;
        // 解析报文
        InAggregateRequestVo inAggregateRequestVo = new InAggregateRequestVo();
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

            ajaxResult = inAggregateService.aggregate(inAggregateRequestVo);

        } else {
            String errMsg = "聚合服务报文格式不正确";
            log.error(errMsg);
            ajaxResult = AjaxResult.error(errMsg);
        }
        return ajaxResult;
    }

}
