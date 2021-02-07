package com.ruoyi.integrator.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 应用转发权限对象 in_app_access_forward
 * 
 * @author zhangxiulin
 * @date 2021-02-04
 */
public class InAppAccessForward extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String appFwdId;

    /** 应用ID */
    @Excel(name = "应用ID")
    private String appId;

    /** 转发ID */
    @Excel(name = "转发ID")
    private String fwdId;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setAppFwdId(String appFwdId) 
    {
        this.appFwdId = appFwdId;
    }

    public String getAppFwdId() 
    {
        return appFwdId;
    }
    public void setAppId(String appId) 
    {
        this.appId = appId;
    }

    public String getAppId() 
    {
        return appId;
    }
    public void setFwdId(String fwdId) 
    {
        this.fwdId = fwdId;
    }

    public String getFwdId() 
    {
        return fwdId;
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
            .append("appFwdId", getAppFwdId())
            .append("appId", getAppId())
            .append("fwdId", getFwdId())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
