package com.ruoyi.integrator.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 转发配置对象 in_forward_info
 * 
 * @author zhangxiulin
 * @date 2020-12-02
 */
public class InForwardInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String infoId;

    /** 功能名称 */
    @Excel(name = "功能名称")
    private String forwardName;

    /** 转发编号 */
    @Excel(name = "转发编号")
    private String forwardCode;

    /** 是否异步 */
    @Excel(name = "是否异步")
    private String isAsync;

    /** 转发类型 */
    @Excel(name = "转发类型")
    private String forwardType;

    /** 转发协议 */
    @Excel(name = "转发协议")
    private String forwardProtocol;

    /** 转发URL */
    private String forwardUrl;

    /** 方法类型 */
    private String forwardMethod;

    /** SQL */
    private String forwardSql;

    /** 数据源 */
    private String forwardDatasource;

    /** 变量 */
    private String forwardVar;

    /** 认证启用 */
    @Excel(name = "认证启用")
    private String authEnabled;

    /** 认证类型 */
    @Excel(name = "认证类型")
    private String authType;

    /** 认证源 */
    @Excel(name = "认证源")
    private String authSource;

    /** 预编译STMT */
    private String isPreparedStatement;

    /** 自定义成功码 */
    private String rtStatusOk;

    /** 自定义失败码 */
    private String rtStatusError;

    /** 启用状态（0正常 1停用） */
    @Excel(name = "启用状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setInfoId(String infoId) 
    {
        this.infoId = infoId;
    }

    public String getInfoId() 
    {
        return infoId;
    }
    public void setForwardName(String forwardName) 
    {
        this.forwardName = forwardName;
    }

    public String getForwardName() 
    {
        return forwardName;
    }
    public void setForwardCode(String forwardCode) 
    {
        this.forwardCode = forwardCode;
    }

    public String getForwardCode() 
    {
        return forwardCode;
    }
    public void setIsAsync(String isAsync) 
    {
        this.isAsync = isAsync;
    }

    public String getIsAsync() 
    {
        return isAsync;
    }
    public void setForwardProtocol(String forwardProtocol) 
    {
        this.forwardProtocol = forwardProtocol;
    }

    public String getForwardProtocol() 
    {
        return forwardProtocol;
    }
    public void setForwardType(String forwardType) 
    {
        this.forwardType = forwardType;
    }

    public String getForwardType() 
    {
        return forwardType;
    }
    public void setForwardUrl(String forwardUrl) 
    {
        this.forwardUrl = forwardUrl;
    }

    public String getForwardUrl() 
    {
        return forwardUrl;
    }
    public void setForwardMethod(String forwardMethod) 
    {
        this.forwardMethod = forwardMethod;
    }

    public String getForwardMethod() 
    {
        return forwardMethod;
    }
    public void setForwardSql(String forwardSql) 
    {
        this.forwardSql = forwardSql;
    }

    public String getForwardSql() 
    {
        return forwardSql;
    }
    public void setForwardDatasource(String forwardDatasource) 
    {
        this.forwardDatasource = forwardDatasource;
    }

    public String getForwardDatasource() 
    {
        return forwardDatasource;
    }
    public void setForwardVar(String forwardVar) 
    {
        this.forwardVar = forwardVar;
    }

    public String getForwardVar() 
    {
        return forwardVar;
    }
    public void setIsPreparedStatement(String isPreparedStatement) 
    {
        this.isPreparedStatement = isPreparedStatement;
    }

    public String getIsPreparedStatement() 
    {
        return isPreparedStatement;
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

    public String getAuthEnabled() {
        return authEnabled;
    }

    public void setAuthEnabled(String authEnabled) {
        this.authEnabled = authEnabled;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthSource() {
        return authSource;
    }

    public void setAuthSource(String authSource) {
        this.authSource = authSource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("infoId", getInfoId())
            .append("forwardName", getForwardName())
            .append("forwardCode", getForwardCode())
            .append("isAsync", getIsAsync())
            .append("forwardProtocol", getForwardProtocol())
            .append("forwardType", getForwardType())
            .append("forwardUrl", getForwardUrl())
            .append("forwardMethod", getForwardMethod())
            .append("forwardSql", getForwardSql())
            .append("forwardDatasource", getForwardDatasource())
            .append("forwardVar", getForwardVar())
            .append("authEnabled", getAuthEnabled())
            .append("authType", getAuthType())
            .append("authSource", getAuthSource())
            .append("isPreparedStatement", getIsPreparedStatement())
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
