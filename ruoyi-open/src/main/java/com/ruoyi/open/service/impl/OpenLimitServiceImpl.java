package com.ruoyi.open.service.impl;

import com.ruoyi.integrator.service.IInAppAccessAuthService;
import com.ruoyi.open.service.IOpenLimitService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @description: 权限
 * @author: zhangxiulin
 * @time: 2021/2/7 22:03
 */
@Service
public class OpenLimitServiceImpl implements IOpenLimitService {

    private static final Logger log = LoggerFactory.getLogger(OpenLimitServiceImpl.class);

    @Autowired
    private IInAppAccessAuthService inAppAccessAuthService;

    @Override
    public boolean forwardLimit(HttpServletRequest httpServletRequest, HttpServletResponse httpResponse, String forwardCode) {
        String authorization = httpServletRequest.getHeader("Authorization");
        log.info("authorization:{}", authorization);
        if (authorization != null) {
            if (authorization.startsWith(OpenDigestServiceImpl.DIGEST.trim())) {
                HashMap<String, String> authFields = OpenDigestServiceImpl.splitAuthFields(authorization.substring(7));
                String username = authFields.get(OpenDigestServiceImpl.USERNAME);
                boolean authForward = inAppAccessAuthService.authForward(username, forwardCode);
                if (authForward) {
                    return true;
                } else {
                    log.error("接入应用[" + username + "]无["+forwardCode+"]转发权限");
                    httpResponse.setStatus(HttpStatus.SC_FORBIDDEN);
                    return false;
                }
            }
            // TODO 其他情况
        }
        return false;
    }

    @Override
    public boolean aggrLimit(HttpServletRequest httpServletRequest, HttpServletResponse httpResponse, String aggrCode) {
        String authorization = httpServletRequest.getHeader("Authorization");
        log.info("authorization:{}", authorization);
        if (authorization != null) {
            if (authorization.startsWith(OpenDigestServiceImpl.DIGEST.trim())) {
                HashMap<String, String> authFields = OpenDigestServiceImpl.splitAuthFields(authorization.substring(7));
                String username = authFields.get(OpenDigestServiceImpl.USERNAME);
                boolean authForward = inAppAccessAuthService.authAggr(username, aggrCode);
                if (authForward) {
                    return true;
                } else {
                    log.error("接入应用[" + username + "]无["+aggrCode+"]聚合权限");
                    httpResponse.setStatus(HttpStatus.SC_FORBIDDEN);
                    return false;
                }
            }
            // TODO 其他情况
        }
        return false;
    }
}
