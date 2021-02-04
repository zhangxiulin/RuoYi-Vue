package com.ruoyi.integrator.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 接入应用对象 in_app_access
 * 
 * @author zhangxiulin
 * @date 2021-02-01
 */
public class InAppAccess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String appId;

    /** 接入标识 */
    @Excel(name = "接入标识")
    private String appKey;

    /** 接入密码 */
    @Excel(name = "接入密码")
    private String appSecret;

    /** 中文名称 */
    @Excel(name = "中文名称")
    private String appCnName;

    /** 英文缩写 */
    @Excel(name = "英文缩写")
    private String appEnCode;

    /** 负责人 */
    @Excel(name = "负责人")
    private String owner;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String ownerPhone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String ownerEmail;

    /** 启用状态（0正常 1停用） */
    @Excel(name = "启用状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setAppId(String appId) 
    {
        this.appId = appId;
    }

    public String getAppId() 
    {
        return appId;
    }
    public void setAppKey(String appKey) 
    {
        this.appKey = appKey;
    }

    public String getAppKey() 
    {
        return appKey;
    }
    public void setAppSecret(String appSecret) 
    {
        this.appSecret = appSecret;
    }

    public String getAppSecret() 
    {
        return appSecret;
    }
    public void setAppCnName(String appCnName) 
    {
        this.appCnName = appCnName;
    }

    public String getAppCnName() 
    {
        return appCnName;
    }
    public void setAppEnCode(String appEnCode) 
    {
        this.appEnCode = appEnCode;
    }

    public String getAppEnCode() 
    {
        return appEnCode;
    }
    public void setOwner(String owner) 
    {
        this.owner = owner;
    }

    public String getOwner() 
    {
        return owner;
    }
    public void setOwnerPhone(String ownerPhone) 
    {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerPhone() 
    {
        return ownerPhone;
    }
    public void setOwnerEmail(String ownerEmail) 
    {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerEmail() 
    {
        return ownerEmail;
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
            .append("appId", getAppId())
            .append("appKey", getAppKey())
            .append("appSecret", getAppSecret())
            .append("appCnName", getAppCnName())
            .append("appEnCode", getAppEnCode())
            .append("owner", getOwner())
            .append("ownerPhone", getOwnerPhone())
            .append("ownerEmail", getOwnerEmail())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
