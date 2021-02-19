package com.ruoyi.integrator.utils;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.domain.vo.InHttpAuthInfoVo;
import com.ruoyi.integrator.thread.RestForwardSendThread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 聚合服务工具类
 * @author: zhangxiulin
 * @time: 2021/2/18 20:58
 */
public class AggregateUtils {

    public static final String KEY_TRANSIENT_IN_HTTP_AUTH_INFO = "transientHttpAuthInfo";

    public static InForwardRequestVo genInForwardRequestVo(InForwardInfo inForwardInfo, InHttpAuthInfoVo inHttpAuthInfoVo, int len, int i, List sendVarList, List sendDataList) {
        Map<String, Object> sendVar = new HashMap<>();
        if (sendVarList != null && sendVarList.size() == len) {
            sendVar = (Map<String, Object>) sendVarList.get(i);
        }
        Object sendData = sendDataList.get(i);
        InForwardRequestVo inForwardRequestVo = new InForwardRequestVo();
        inForwardRequestVo.setReqId(inForwardInfo.getForwardCode());
        inForwardRequestVo.setInForwardInfo(inForwardInfo);
        inForwardRequestVo.setVar(sendVar);
        inForwardRequestVo.setInHttpAuthInfoVo(inHttpAuthInfoVo);
        if (sendData instanceof List){
            inForwardRequestVo.setDataList((List)sendData);
        } else {
            inForwardRequestVo.setData((Map) sendData);
        }
        return inForwardRequestVo;
    }

    // 移除httpAuthInfo
    public static void removeHttpAuthInfo (AjaxResult fwdAjaxResult){
        if (fwdAjaxResult.containsKey(KEY_TRANSIENT_IN_HTTP_AUTH_INFO)) {
            fwdAjaxResult.remove(KEY_TRANSIENT_IN_HTTP_AUTH_INFO);
        }
    }

}
