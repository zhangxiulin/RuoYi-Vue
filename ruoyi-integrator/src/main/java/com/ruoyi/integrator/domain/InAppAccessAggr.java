package com.ruoyi.integrator.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 应用聚合权限对象 in_app_access_aggr
 * 
 * @author zhangxiulin
 * @date 2021-02-07
 */
public class InAppAccessAggr extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String appAggrId;

    /** 应用ID */
    @Excel(name = "应用ID")
    private String appId;

    /** 聚合ID */
    @Excel(name = "聚合ID")
    private String aggrId;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setAppAggrId(String appAggrId)
    {
        this.appAggrId = appAggrId;
    }

    public String getAppAggrId()
    {
        return appAggrId;
    }
    public void setAppId(String appId) 
    {
        this.appId = appId;
    }

    public String getAppId() 
    {
        return appId;
    }
    public void setAggrId(String aggrId)
    {
        this.aggrId = aggrId;
    }

    public String getAggrId()
    {
        return aggrId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("appAggrId", getAppAggrId())
            .append("appId", getAppId())
            .append("aggrId", getAggrId())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
