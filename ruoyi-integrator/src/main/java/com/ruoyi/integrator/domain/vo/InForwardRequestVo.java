package com.ruoyi.integrator.domain.vo;

import com.alibaba.fastjson.JSONObject;

/**
 * @description: 前置转发请求对象
 * @author: zhangxiulin
 * @time: 2020/11/27 12:41
 */
public class InForwardRequestVo {

    private String reqId;

    private JSONObject params;

    private JSONObject data;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
