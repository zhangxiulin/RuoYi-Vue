package com.ruoyi.integrator.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据源对象 in_datasource
 * 
 * @author zhangxiulin
 * @date 2020-12-09
 */
public class InDatasource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String datasourceId;

    /** 数据源名称 */
    @Excel(name = "数据源名称")
    private String datasourceName;

    /** 数据源配置 */
    @Excel(name = "数据源配置")
    private String datasourceOptions;

    /** 启用状态（0正常 1停用） */
    @Excel(name = "启用状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setDatasourceId(String datasourceId) 
    {
        this.datasourceId = datasourceId;
    }

    public String getDatasourceId() 
    {
        return datasourceId;
    }
    public void setDatasourceName(String datasourceName) 
    {
        this.datasourceName = datasourceName;
    }

    public String getDatasourceName() 
    {
        return datasourceName;
    }
    public void setDatasourceOptions(String datasourceOptions) 
    {
        this.datasourceOptions = datasourceOptions;
    }

    public String getDatasourceOptions() 
    {
        return datasourceOptions;
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
            .append("datasourceId", getDatasourceId())
            .append("datasourceName", getDatasourceName())
            .append("datasourceOptions", getDatasourceOptions())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
