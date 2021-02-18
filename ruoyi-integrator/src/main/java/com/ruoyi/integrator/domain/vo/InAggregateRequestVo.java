package com.ruoyi.integrator.domain.vo;

import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.domain.InForwardInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/27 17:40
 */
public class InAggregateRequestVo {

    private String reqId;

    private List varList;

    private List dataList;

    /**过程数据**/
    private InAggregation inAggregation;

    private InHttpAuthInfoVo inHttpAuthInfoVo;
    /**过程数据**/

    @NotBlank(message = "聚合服务编号不能为空")
    @Size(min = 0, max = 30, message = "聚合服务编号长度不能超过30个字符")
    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public List getVarList() {
        return varList;
    }

    public void setVarList(List varList) {
        this.varList = varList;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public InAggregation getInAggregation() {
        return inAggregation;
    }

    public void setInAggregation(InAggregation inAggregation) {
        this.inAggregation = inAggregation;
    }

    public InHttpAuthInfoVo getInHttpAuthInfoVo() {
        return inHttpAuthInfoVo;
    }

    public void setInHttpAuthInfoVo(InHttpAuthInfoVo inHttpAuthInfoVo) {
        this.inHttpAuthInfoVo = inHttpAuthInfoVo;
    }
}
