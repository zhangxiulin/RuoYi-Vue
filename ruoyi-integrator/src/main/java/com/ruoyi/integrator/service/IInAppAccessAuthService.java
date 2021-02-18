package com.ruoyi.integrator.service;

public interface IInAppAccessAuthService {

    boolean authForward(String appKey, String forwardCode);

    boolean authAggr(String appKey, String aggrCode);

}
