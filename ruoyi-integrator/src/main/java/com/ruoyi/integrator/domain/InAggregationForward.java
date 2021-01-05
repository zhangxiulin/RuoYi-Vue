package com.ruoyi.integrator.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 服务聚合关联对象 in_aggregation_forward
 *
 * @author zhangxiulin
 * @date 2020-12-24
 */
public class InAggregationForward extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String agrFwdId;

    /** 聚合ID */
    @Excel(name = "聚合ID")
    private String agrId;

    /** 转发ID */
    @Excel(name = "转发ID")
    private String fwdId;

    /** 顺序 */
    @Excel(name = "顺序")
    private Integer orderNum;

    public void setAgrFwdId(String agrFwdId)
    {
        this.agrFwdId = agrFwdId;
    }

    public String getAgrFwdId()
    {
        return agrFwdId;
    }
    public void setAgrId(String agrId)
    {
        this.agrId = agrId;
    }

    public String getAgrId()
    {
        return agrId;
    }
    public void setFwdId(String fwdId)
    {
        this.fwdId = fwdId;
    }

    public String getFwdId()
    {
        return fwdId;
    }
    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("agrFwdId", getAgrFwdId())
                .append("agrId", getAgrId())
                .append("fwdId", getFwdId())
                .append("orderNum", getOrderNum())
                .toString();
    }
}