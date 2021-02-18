package com.ruoyi.integrator.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 服务聚合对象 in_aggregation
 * 
 * @author zhangxiulin
 * @date 2020-12-23
 */
public class InAggregation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String aggrId;

    /** 聚合名称 */
    @Excel(name = "聚合名称")
    private String aggrName;

    /** 聚合编号 */
    @Excel(name = "聚合编号")
    private String aggrCode;

    /** 分布式事务 */
    @Excel(name = "分布式事务")
    private String isDtx;

    /** 事务方案 */
    @Excel(name = "事务方案")
    private String dtxSolution;

    /** 是否异步 */
    @Excel(name = "是否异步")
    private String isAsync;

    /** 执行顺序 */
    @Excel(name = "执行顺序")
    private String executionOrder;

    /** 自定义成功码 */
    @Excel(name = "自定义成功码")
    private String rtStatusOk;

    /** 自定义失败码 */
    @Excel(name = "自定义失败码")
    private String rtStatusError;

    /** 启用状态 */
    @Excel(name = "启用状态")
    private String status;

    /**服务接口**/
    private String[] forwardIds;

    /**服务接口列表**/
    private List<InForwardInfo> forwardInfoList;

    public void setAggrId(String aggrId)
    {
        this.aggrId = aggrId;
    }

    public String getAggrId()
    {
        return aggrId;
    }
    public void setAggrName(String aggrName)
    {
        this.aggrName = aggrName;
    }

    public String getAggrName()
    {
        return aggrName;
    }
    public void setAggrCode(String aggrCode)
    {
        this.aggrCode = aggrCode;
    }

    public String getAggrCode()
    {
        return aggrCode;
    }
    public void setIsDtx(String isDtx) 
    {
        this.isDtx = isDtx;
    }

    public String getIsDtx() 
    {
        return isDtx;
    }
    public void setIsAsync(String isAsync) 
    {
        this.isAsync = isAsync;
    }

    public String getIsAsync() 
    {
        return isAsync;
    }
    public void setExecutionOrder(String executionOrder) 
    {
        this.executionOrder = executionOrder;
    }

    public String getExecutionOrder() 
    {
        return executionOrder;
    }
    public void setRtStatusOk(String rtStatusOk) 
    {
        this.rtStatusOk = rtStatusOk;
    }

    public String getRtStatusOk() 
    {
        return rtStatusOk;
    }
    public void setRtStatusError(String rtStatusError) 
    {
        this.rtStatusError = rtStatusError;
    }

    public String getRtStatusError() 
    {
        return rtStatusError;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public String[] getForwardIds() {
        return forwardIds;
    }

    public void setForwardIds(String[] forwardIds) {
        this.forwardIds = forwardIds;
    }

    public String getDtxSolution() {
        return dtxSolution;
    }

    public void setDtxSolution(String dtxSolution) {
        this.dtxSolution = dtxSolution;
    }

    public List<InForwardInfo> getForwardInfoList() {
        return forwardInfoList;
    }

    public void setForwardInfoList(List<InForwardInfo> forwardInfoList) {
        this.forwardInfoList = forwardInfoList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("aggrId", getAggrId())
            .append("aggrName", getAggrName())
            .append("aggrCode", getAggrCode())
            .append("isDtx", getIsDtx())
            .append("dtxSolution", getDtxSolution())
            .append("isAsync", getIsAsync())
            .append("executionOrder", getExecutionOrder())
            .append("rtStatusOk", getRtStatusOk())
            .append("rtStatusError", getRtStatusError())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
