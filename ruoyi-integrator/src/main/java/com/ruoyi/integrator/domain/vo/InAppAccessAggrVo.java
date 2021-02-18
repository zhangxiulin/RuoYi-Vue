package com.ruoyi.integrator.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.integrator.domain.InAppAccessAggr;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2021/2/8 12:26
 */
public class InAppAccessAggrVo extends InAppAccessAggr {

    @Excel(name = "应用")
    private String appKey;

    @Excel(name = "转发")
    private String aggrCode;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAggrCode() {
        return aggrCode;
    }

    public void setAggrCode(String aggrCode) {
        this.aggrCode = aggrCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("appAggrId", getAppAggrId())
                .append("appId", getAppId())
                .append("aggrId", getAggrId())
                .append("appKey", getAppKey())
                .append("aggrCode", getAggrCode())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }

}
