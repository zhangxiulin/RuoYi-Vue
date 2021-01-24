package com.ruoyi.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2021/1/23 19:47
 */
@Configuration
public class TccConfig {

    /**TCC confirm url**/
    @Value("${ruoyix.tcc.confirmUrl}")
    private String tccConfirmUrl;

    @Value("${ruoyix.tcc.cancelUrl}")
    private String tccCancelUrl;

    @Value("${ruoyix.tcc.expiresDelayMs}")
    private Integer tccExpiresDelayMs;

    public void setTccConfirmUrl(String tccConfirmUrl) {
        this.tccConfirmUrl = tccConfirmUrl;
    }

    public void setTccExpiresDelayMs(Integer tccExpiresDelayMs) {
        this.tccExpiresDelayMs = tccExpiresDelayMs;
    }

    public String getTccConfirmUrl() {
        return this.tccConfirmUrl;
    }

    public Integer getTccExpiresDelayMs() {
        return this.tccExpiresDelayMs;
    }

    public String getTccCancelUrl() {
        return tccCancelUrl;
    }

    public void setTccCancelUrl(String tccCancelUrl) {
        this.tccCancelUrl = tccCancelUrl;
    }
}
