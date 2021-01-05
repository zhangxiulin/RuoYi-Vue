package com.ruoyi.integrator.domain.vo;

import com.ruoyi.integrator.domain.InForwardInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * @description: 前置转发请求对象
 * @author: zhangxiulin
 * @time: 2020/11/27 12:41
 */
public class InForwardRequestVo {

    private String reqId;

    private Map<String, Object> var;

    private Map<String, Object> data;

    private List dataList;

    /**过程数据**/
    private InForwardInfo inForwardInfo;

    private Map<String, Object> relateData;
    /**过程数据**/

    @NotBlank(message = "转发编号不能为空")
    @Size(min = 0, max = 30, message = "转发编号长度不能超过30个字符")
    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Map<String, Object> getVar() {
        return var;
    }

    public void setVar(Map<String, Object> var) {
        this.var = var;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public InForwardInfo getInForwardInfo() {
        return inForwardInfo;
    }

    public void setInForwardInfo(InForwardInfo inForwardInfo) {
        this.inForwardInfo = inForwardInfo;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public Map<String, Object> getRelateData() {
        return relateData;
    }

    public void setRelateData(Map<String, Object> relateData) {
        this.relateData = relateData;
    }
}
