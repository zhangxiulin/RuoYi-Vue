package com.ruoyi.integrator.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInForwardInfoService;
import com.ruoyi.integrator.service.IInForwardService;
import com.ruoyi.integrator.service.IInForwardStrategy;
import com.ruoyi.integrator.service.InForwardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 转发
 * @author: zhangxiulin
 * @time: 2020/11/27 13:28
 */
@Service
public class InForwardServiceImpl implements IInForwardService {

    private static final Logger log = LoggerFactory.getLogger(InForwardServiceImpl.class);

    private static final String KEY_REQ_ID = "reqId";
    private static final String KEY_VAR = "var";
    private static final String KEY_DATA = "data";

    @Autowired
    private InForwardContext inForwardContext;

    @Autowired
    private IInForwardInfoService inForwardInfoService;

    @Autowired
    private Validator validator;

    @Override
    public AjaxResult forward(JSONObject jsonObject) {
        AjaxResult ajaxResult = null;
        // 解析报文
        InForwardRequestVo inForwardRequestVo = new InForwardRequestVo();
        if (jsonObject != null){
            String reqId = jsonObject.getString(KEY_REQ_ID);
            inForwardRequestVo.setReqId(reqId);
            inForwardRequestVo.setVar(jsonObject.getJSONObject(KEY_VAR));
            if (jsonObject.get(KEY_DATA) instanceof Map){   // data报文是普通JSON
                log.info("本次请求["+reqId+"]报文内data格式：Map");
                inForwardRequestVo.setData(jsonObject.getJSONObject(KEY_DATA));
            } else if (jsonObject.get(KEY_DATA) instanceof List){   // data报文是数组
                log.info("本次请求["+reqId+"]报文内data格式：数组");
                inForwardRequestVo.setDataList(jsonObject.getJSONArray(KEY_DATA));
            } else { // 可能是字符串，尝试转换为JSON
                log.info("本次请求["+reqId+"]报文内data格式不确定，尝试转换为JSON");
                inForwardRequestVo.setData(jsonObject.getJSONObject(KEY_DATA));
            }

            // 校验报文
            Set<ConstraintViolation<InForwardRequestVo>> constraintViolationSet = validator.validate(inForwardRequestVo);
            if (constraintViolationSet.size() > 0){
                StringBuilder errMsgSb = new StringBuilder();
                for (ConstraintViolation<InForwardRequestVo> v : constraintViolationSet){
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
        }

        // 根据转发编号获取转发配置信息
        InForwardInfo inForwardInfo = inForwardInfoService.selectInForwardInfoByCode(inForwardRequestVo.getReqId());
        if (inForwardInfo != null){
            inForwardRequestVo.setInForwardInfo(inForwardInfo);
            InForwardType inForwardType = InForwardType.valueOf(inForwardInfo.getForwardType());
            if (inForwardType != null){
                ajaxResult = this.forward(inForwardType, inForwardRequestVo);
                if (ajaxResult == null){
                    ajaxResult = AjaxResult.error("转发器返回null");
                }
            }else{
                ajaxResult = AjaxResult.error("转发配置信息不正确");
            }
        } else{
            ajaxResult = AjaxResult.error("未找到转发["+inForwardRequestVo.getReqId()+"]配置信息");
        }

        return ajaxResult;
    }

    @Override
    public AjaxResult forward(InForwardType type, InForwardRequestVo request  ) {
        return inForwardContext.forward(type, request);
    }

}
