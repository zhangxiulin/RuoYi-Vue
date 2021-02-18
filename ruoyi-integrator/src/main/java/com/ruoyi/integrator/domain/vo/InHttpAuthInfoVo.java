package com.ruoyi.integrator.domain.vo;

import com.ruoyi.common.enums.HttpAuthenticationType;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2021/2/17 1:31
 */
public class InHttpAuthInfoVo {

    private boolean specifyEnabled;

    private HttpAuthenticationType specifyAuthType;

    private String  specifyToken;

    private String  specifyUsername;

    private String  specifyPassword;

    public boolean getSpecifyEnabled() {
        return specifyEnabled;
    }

    public void setSpecifyEnabled(boolean specifyEnabled) {
        this.specifyEnabled = specifyEnabled;
    }

    public HttpAuthenticationType getSpecifyAuthType() {
        return specifyAuthType;
    }

    public void setSpecifyAuthType(HttpAuthenticationType specifyAuthType) {
        this.specifyAuthType = specifyAuthType;
    }

    public String getSpecifyToken() {
        return specifyToken;
    }

    public void setSpecifyToken(String specifyToken) {
        this.specifyToken = specifyToken;
    }

    public String getSpecifyUsername() {
        return specifyUsername;
    }

    public void setSpecifyUsername(String specifyUsername) {
        this.specifyUsername = specifyUsername;
    }

    public String getSpecifyPassword() {
        return specifyPassword;
    }

    public void setSpecifyPassword(String specifyPassword) {
        this.specifyPassword = specifyPassword;
    }
}
