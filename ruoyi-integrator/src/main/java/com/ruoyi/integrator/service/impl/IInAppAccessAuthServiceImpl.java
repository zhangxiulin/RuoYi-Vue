package com.ruoyi.integrator.service.impl;

import com.ruoyi.integrator.mapper.InAppAccessAggrMapper;
import com.ruoyi.integrator.mapper.InAppAccessForwardMapper;
import com.ruoyi.integrator.service.IInAppAccessAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 接入应用权限
 * @author: zhangxiulin
 * @time: 2021/2/7 21:31
 */
@Service
public class IInAppAccessAuthServiceImpl implements IInAppAccessAuthService {

    private static final Logger log = LoggerFactory.getLogger(IInAppAccessAuthServiceImpl.class);

    @Autowired
    private InAppAccessForwardMapper inAppAccessForwardMapper;

    @Autowired
    private InAppAccessAggrMapper inAppAccessAggrMapper;

    @Override
    public boolean authForward(String appKey, String forwardCode) {
        Integer count = inAppAccessForwardMapper.countInAppAccessForward(appKey, forwardCode);
        if (count > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean authAggr(String appKey, String aggrCode) {
        Integer count = inAppAccessAggrMapper.countInAppAccessAggr(appKey, aggrCode);
        if (count > 0){
            return true;
        }
        return false;
    }
}
