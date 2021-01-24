package com.ruoyi.demo.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * TCC动账流水对象 demo_account_water_tcc
 * 
 * @author zhangxiulin
 * @date 2021-01-21
 */
public class DemoAccountWaterTcc extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 流水号 */
    @Excel(name = "流水号")
    private String serialNumber;

    /** 客户号 */
    @Excel(name = "客户号")
    private String userCode;

    /** 借贷标志 */
    @Excel(name = "借贷标志")
    private String jdFlag;

    /** 借金额 */
    @Excel(name = "借金额")
    private BigDecimal jMoney;

    /** 贷金额 */
    @Excel(name = "贷金额")
    private BigDecimal dMoney;

    /** TCC阶段 */
    @Excel(name = "TCC阶段")
    private String tccStage;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSerialNumber(String serialNumber) 
    {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() 
    {
        return serialNumber;
    }
    public void setUserCode(String userCode) 
    {
        this.userCode = userCode;
    }

    public String getUserCode() 
    {
        return userCode;
    }
    public void setJdFlag(String jdFlag) 
    {
        this.jdFlag = jdFlag;
    }

    public String getJdFlag() 
    {
        return jdFlag;
    }
    public void setjMoney(BigDecimal jMoney) 
    {
        this.jMoney = jMoney;
    }

    public BigDecimal getjMoney() 
    {
        return jMoney;
    }
    public void setdMoney(BigDecimal dMoney) 
    {
        this.dMoney = dMoney;
    }

    public BigDecimal getdMoney() 
    {
        return dMoney;
    }
    public void setTccStage(String tccStage) 
    {
        this.tccStage = tccStage;
    }

    public String getTccStage() 
    {
        return tccStage;
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
            .append("id", getId())
            .append("serialNumber", getSerialNumber())
            .append("userCode", getUserCode())
            .append("jdFlag", getJdFlag())
            .append("jMoney", getjMoney())
            .append("dMoney", getdMoney())
            .append("tccStage", getTccStage())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
