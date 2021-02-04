package com.ruoyi.demo.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * TCC动账流水对象 demo_account_water_tcc
 * 
 * @author zhangxiulin
 * @date 2021-01-21
 */
public class DemoAccountWaterTccTryVo
{
    private static final long serialVersionUID = 1L;

    /** 客户号 */
    private String account;

    /** 借贷标志 */
    private String jdFlag;

    /** 借、贷金额 */
    private BigDecimal money;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getJdFlag() {
        return jdFlag;
    }

    public void setJdFlag(String jdFlag) {
        this.jdFlag = jdFlag;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("account", getAccount())
            .append("jdFlag", getJdFlag())
            .append("money", getMoney())
            .toString();
    }
}
