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
    private String aggrFwdId;

    /** 聚合ID */
    @Excel(name = "聚合ID")
    private String aggrId;

    /** 转发ID */
    @Excel(name = "转发ID")
    private String fwdId;

    /** 顺序 */
    @Excel(name = "顺序")
    private Integer orderNum;

    public void setAggrFwdId(String aggrFwdId)
    {
        this.aggrFwdId = aggrFwdId;
    }

    public String getAggrFwdId()
    {
        return aggrFwdId;
    }
    public void setAggrId(String aggrId)
    {
        this.aggrId = aggrId;
    }

    public String getAggrId()
    {
        return aggrId;
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
                .append("aggrFwdId", getAggrFwdId())
                .append("aggrId", getAggrId())
                .append("fwdId", getFwdId())
                .append("orderNum", getOrderNum())
                .toString();
    }
}