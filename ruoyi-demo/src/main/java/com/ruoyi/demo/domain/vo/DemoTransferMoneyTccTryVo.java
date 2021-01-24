package com.ruoyi.demo.domain.vo;

import java.math.BigDecimal;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2021/1/21 12:50
 */
public class DemoTransferMoneyTccTryVo {

    private String fromAccount;

    private String toAccount;

    private BigDecimal money;

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
