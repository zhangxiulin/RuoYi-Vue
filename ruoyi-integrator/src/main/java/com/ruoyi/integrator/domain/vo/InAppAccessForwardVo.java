package com.ruoyi.integrator.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.integrator.domain.InAppAccessForward;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 应用转发权限对象 in_app_access_forward
 * 
 * @author zhangxiulin
 * @date 2021-02-04
 */
public class InAppAccessForwardVo extends InAppAccessForward
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "应用")
    private String appKey;

    @Excel(name = "转发")
    private String fwdCode;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getFwdCode() {
        return fwdCode;
    }

    public void setFwdCode(String fwdCode) {
        this.fwdCode = fwdCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("appFwdId", getAppFwdId())
            .append("appId", getAppId())
            .append("fwdId", getFwdId())
            .append("appKey", getAppKey())
            .append("fwdCode", getFwdCode())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
