package com.ruoyi.integrator.thread;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.HttpAuthenticationType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.digest.HttpDigestRequestUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.integrator.domain.InAppAccess;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InHttpAuthInfoVo;
import com.ruoyi.integrator.enums.InForwardMethod;
import com.ruoyi.integrator.enums.InForwardProtocol;
import com.ruoyi.integrator.service.IInAppAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/8 12:42
 */
public class RestForwardSendThread implements Callable<AjaxResult> {

    private static final Logger logger = LoggerFactory.getLogger(RestForwardSendThread.class);

    private InForwardInfo inForwardInfo;

    private Map<String, Object> sendVar;

    private Map<String, Object> sendData;

    private static final String KEY_USERNAME = "username";

    private static final String KEY_PASSWORD = "password";

    public static final String KEY_IN_HTTP_AUTH_INFO = "httpAuthInfo";

    /**认证信息**/
    private InHttpAuthInfoVo inHttpAuthInfoVo = new InHttpAuthInfoVo();

    private boolean rtHttpAuthInfo;

    private InHttpAuthInfoVo newInHttpAuthInfoVo;
    /**认证信息**/

    public RestForwardSendThread(InForwardInfo inForwardInfo, Map<String, Object> sendVar, Map<String, Object> sendData){
        this.inForwardInfo = inForwardInfo;
        this.sendVar = sendVar;
        this.sendData = sendData;
    }

    public RestForwardSendThread(InForwardInfo inForwardInfo, Map<String, Object> sendVar, Map<String, Object> sendData, InHttpAuthInfoVo inHttpAuthInfoVo){
        this.inForwardInfo = inForwardInfo;
        this.sendVar = sendVar;
        this.sendData = sendData;
        this.inHttpAuthInfoVo = inHttpAuthInfoVo;
    }

    public RestForwardSendThread(InForwardInfo inForwardInfo, Map<String, Object> sendVar, Map<String, Object> sendData, InHttpAuthInfoVo inHttpAuthInfoVo, boolean rtHttpAuthInfo){
        this.inForwardInfo = inForwardInfo;
        this.sendVar = sendVar;
        this.sendData = sendData;
        this.inHttpAuthInfoVo = inHttpAuthInfoVo;
        this.rtHttpAuthInfo = rtHttpAuthInfo;
    }

    @Override
    public AjaxResult call() {
        AjaxResult result = null;
        logger.info("开始处理[REST]转发服务...");
        if (inForwardInfo != null){
            // 区分协议，目前只支持http
            if (StringUtils.isNotEmpty(inForwardInfo.getForwardProtocol())){
                InForwardProtocol forwardProtocolEnum = InForwardProtocol.valueOf(inForwardInfo.getForwardProtocol());
                switch (forwardProtocolEnum){
                    case HTTP:
                    {
                        boolean specifyEnabled = inHttpAuthInfoVo.getSpecifyEnabled();
                        HttpAuthenticationType specifyAuthType = inHttpAuthInfoVo.getSpecifyAuthType();
                        String specifyToken = inHttpAuthInfoVo.getSpecifyToken();
                        String specifyUsername = inHttpAuthInfoVo.getSpecifyUsername();
                        String specifyPassword = inHttpAuthInfoVo.getSpecifyPassword();
                        logger.info("转发协议[HTTP] 转发编号[" + inForwardInfo.getForwardCode() + "]");
                        if (StringUtils.isNotEmpty(inForwardInfo.getForwardMethod())){
                            InForwardMethod forwardMethodEnum = InForwardMethod.valueOf(inForwardInfo.getForwardMethod());
                            if (InForwardMethod.GET == forwardMethodEnum){
                                logger.debug("HTTP动作[GET] 转发编号[" + inForwardInfo.getForwardCode() + "]");
                                try {
                                    String rtString = "";
                                    // 认证方案有2类，第一类是外部（比如聚合服务）指定的认证方案，第二类是转发信息表里配置的认证方案
                                    // 判断认证方案
                                    if (specifyEnabled) {
                                        // 外部指定的认证方案
                                        logger.debug("HTTP动作[GET] 转发编号[" + inForwardInfo.getForwardCode() + "] 认证方式[" + specifyAuthType.name() + "]");
                                        switch (specifyAuthType) {
                                            case BASIC_AUTH: {
                                                break;
                                            }
                                            case DIGEST_AUTH: {
                                                rtString = HttpDigestRequestUtils.sendGet(inForwardInfo.getForwardUrl(), "", specifyUsername, specifyPassword, "json");
                                                break;
                                            }
                                            case BEARER_TOKEN: {
                                                rtString = HttpUtils.sendGetWithBearerToken(inForwardInfo.getForwardUrl(), sendData, specifyToken);
                                                break;
                                            }
                                            default:
                                                break;
                                        }
                                    } else {
                                        // 转发信息配置的认证方案
                                        String authEnabled = inForwardInfo.getAuthEnabled();
                                        String authType = inForwardInfo.getAuthType();
                                        if (Constants.YES.equals(authEnabled)) {
                                            if (authType != null) {
                                                HttpAuthenticationType authTypeEnum = HttpAuthenticationType.valueOf(authType);
                                                if (authTypeEnum != null) {
                                                    logger.debug("HTTP动作[GET] 转发编号[" + inForwardInfo.getForwardCode() + "] 认证方式[" + authTypeEnum.name() + "]");
                                                    switch (authTypeEnum) {
                                                        case BASIC_AUTH: {
                                                            break;
                                                        }
                                                        case DIGEST_AUTH: {
                                                            // 获取用户名密码
                                                            String authSource = inForwardInfo.getAuthSource();
                                                            Map<String, String> usernamePasswordMap = getUsernamePassword(authSource);
                                                            String username = usernamePasswordMap.get(KEY_USERNAME);
                                                            String password = usernamePasswordMap.get(KEY_PASSWORD);
                                                            rtString = HttpDigestRequestUtils.sendGet(inForwardInfo.getForwardUrl(), "", username, password, "json");
                                                            // 保存当前的认证信息，如果当前线程有下次请求（比如聚合服务的TCC确认或者取消请求）可以使用,请在触发器开启认证标志
                                                            newInHttpAuthInfoVo = setSpecifyDigestAuth(username, password);
                                                            break;
                                                        }
                                                        case BEARER_TOKEN: {
                                                            rtString = HttpUtils.sendGetWithBearerToken(inForwardInfo.getForwardUrl(), sendData, specifyToken);
                                                            // 保存当前的认证信息，如果当前线程有下次请求（比如聚合服务的TCC确认或者取消请求）可以使用，请在触发器开启认证标志
                                                            newInHttpAuthInfoVo = setSpecifyBearerToken(specifyToken);
                                                            break;
                                                        }
                                                        default:
                                                            break;
                                                    }

                                                }
                                            }
                                        } else {
                                            rtString = HttpUtils.sendGet(inForwardInfo.getForwardUrl(), sendData);
                                        }
                                    }
                                    result = AjaxResult.success("转发编号["+inForwardInfo.getForwardCode()+"]转发成功", rtString);
                                } catch (Exception e) {
                                    String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]异常";
                                    logger.error(errMsg, e);
                                    result = AjaxResult.error(errMsg);
                                }
                            } else if (InForwardMethod.POST == forwardMethodEnum){
                                logger.debug("HTTP动作[POST] 转发编号[" + inForwardInfo.getForwardCode() + "]");
                                try {
                                    String rtString = "";
                                    // 认证方案有2类，第一类是外部（比如聚合服务）指定的认证方案，第二类是转发信息表里配置的认证方案
                                    // 判断认证方案
                                    if (specifyEnabled) {
                                        // 外部指定的认证方案
                                        logger.debug("HTTP动作[POST] 转发编号[" + inForwardInfo.getForwardCode() + "] 认证方式[" + specifyAuthType.name() + "]");
                                        switch (specifyAuthType) {
                                            case BASIC_AUTH: {
                                                break;
                                            }
                                            case DIGEST_AUTH: {
                                                rtString = HttpDigestRequestUtils.sendPost(inForwardInfo.getForwardUrl(), "", specifyUsername, specifyPassword, new JSONObject(sendData).toJSONString(), "json");
                                                break;
                                            }
                                            case BEARER_TOKEN: {
                                                rtString = HttpUtils.sendJsonPostWithBearerToken(inForwardInfo.getForwardUrl(), sendData, specifyToken);
                                                break;
                                            }
                                            default:
                                                break;
                                        }
                                    } else {
                                        String authEnabled = inForwardInfo.getAuthEnabled();
                                        String authType = inForwardInfo.getAuthType();
                                        if (Constants.YES.equals(authEnabled)) {
                                            if (authType != null) {
                                                HttpAuthenticationType authTypeEnum = HttpAuthenticationType.valueOf(authType);
                                                if (authTypeEnum != null) {
                                                    logger.debug("HTTP动作[POST] 转发编号[" + inForwardInfo.getForwardCode() + "] 认证方式[" + authTypeEnum.name() + "]");
                                                    switch (authTypeEnum) {
                                                        case BASIC_AUTH: {
                                                            break;
                                                        }
                                                        case DIGEST_AUTH: {
                                                            // 获取用户名密码
                                                            String authSource = inForwardInfo.getAuthSource();
                                                            Map<String, String> usernamePasswordMap = getUsernamePassword(authSource);
                                                            String username = usernamePasswordMap.get(KEY_USERNAME);
                                                            String password = usernamePasswordMap.get(KEY_PASSWORD);
                                                            rtString = HttpDigestRequestUtils.sendPost(inForwardInfo.getForwardUrl(), "", username, password, new JSONObject(sendData).toJSONString(), "json");
                                                            newInHttpAuthInfoVo = setSpecifyDigestAuth(username, password);
                                                            break;
                                                        }
                                                        case BEARER_TOKEN: {
                                                            rtString = HttpUtils.sendJsonPostWithBearerToken(inForwardInfo.getForwardUrl(), sendData, specifyToken);
                                                            newInHttpAuthInfoVo = setSpecifyBearerToken(specifyToken);
                                                            break;
                                                        }
                                                        default:
                                                            break;
                                                    }

                                                }
                                            }
                                        } else {
                                            rtString = HttpUtils.sendJsonPost(inForwardInfo.getForwardUrl(), sendData);
                                        }
                                    }

                                    result = AjaxResult.success("转发编号["+inForwardInfo.getForwardCode()+"]转发成功", rtString);
                                } catch (Exception e) {
                                    String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]异常";
                                    logger.error(errMsg, e);
                                    result = AjaxResult.error(errMsg);
                                }
                            } else if (InForwardMethod.PUT == forwardMethodEnum) {
                                logger.debug("HTTP动作[PUT] 转发编号[" + inForwardInfo.getForwardCode() + "]");
                                try {
                                    String rtString = "";
                                    // 认证方案有2类，第一类是外部（比如聚合服务）指定的认证方案，第二类是转发信息表里配置的认证方案
                                    // 判断认证方案
                                    if (specifyEnabled) {
                                        // 外部指定的认证方案
                                        logger.debug("HTTP动作[PUT] 转发编号[" + inForwardInfo.getForwardCode() + "] 认证方式[" + specifyAuthType.name() + "]");
                                        switch (specifyAuthType) {
                                            case BASIC_AUTH: {
                                                break;
                                            }
                                            case DIGEST_AUTH: {
                                                rtString = HttpDigestRequestUtils.sendPut(inForwardInfo.getForwardUrl(), "", specifyUsername, specifyPassword, new JSONObject(sendData).toJSONString(), "json");
                                                break;
                                            }
                                            case BEARER_TOKEN: {
                                                rtString = HttpUtils.sendJsonPutWithBearerToken(inForwardInfo.getForwardUrl(), sendData, specifyToken);
                                                break;
                                            }
                                            default:
                                                break;
                                        }
                                    } else {
                                        String authEnabled = inForwardInfo.getAuthEnabled();
                                        String authType = inForwardInfo.getAuthType();
                                        if (Constants.YES.equals(authEnabled)) {
                                            if (authType != null) {
                                                HttpAuthenticationType authTypeEnum = HttpAuthenticationType.valueOf(authType);
                                                if (authTypeEnum != null) {
                                                    logger.debug("HTTP动作[PUT] 转发编号[" + inForwardInfo.getForwardCode() + "] 认证方式[" + authTypeEnum.name() + "]");
                                                    switch (authTypeEnum) {
                                                        case BASIC_AUTH: {
                                                            break;
                                                        }
                                                        case DIGEST_AUTH: {
                                                            // 获取用户名密码
                                                            String authSource = inForwardInfo.getAuthSource();
                                                            Map<String, String> usernamePasswordMap = getUsernamePassword(authSource);
                                                            String username = usernamePasswordMap.get(KEY_USERNAME);
                                                            String password = usernamePasswordMap.get(KEY_PASSWORD);
                                                            rtString = HttpDigestRequestUtils.sendPut(inForwardInfo.getForwardUrl(), "", username, password, new JSONObject(sendData).toJSONString(), "json");
                                                            newInHttpAuthInfoVo = setSpecifyDigestAuth(username, password);
                                                            break;
                                                        }
                                                        case BEARER_TOKEN: {
                                                            rtString = HttpUtils.sendJsonPutWithBearerToken(inForwardInfo.getForwardUrl(), sendData, specifyToken);
                                                            newInHttpAuthInfoVo = setSpecifyBearerToken(specifyToken);
                                                            break;
                                                        }
                                                        default:
                                                            break;
                                                    }

                                                }
                                            }
                                        } else {
                                            rtString = HttpUtils.sendJsonPut(inForwardInfo.getForwardUrl(), sendData);
                                        }
                                    }

                                    result = AjaxResult.success("转发编号["+inForwardInfo.getForwardCode()+"]转发成功", rtString);
                                } catch (Exception e) {
                                    String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]异常";
                                    logger.error(errMsg, e);
                                    result = AjaxResult.error(errMsg);
                                }
                            } else if (InForwardMethod.DELETE == forwardMethodEnum) {
                                logger.debug("HTTP动作[DELETE] 转发编号["  + inForwardInfo.getForwardCode() + "]");
                                try {
                                    String rtString = "";
                                    // 认证方案有2类，第一类是外部（比如聚合服务）指定的认证方案，第二类是转发信息表里配置的认证方案
                                    // 判断认证方案
                                    if (specifyEnabled) {
                                        // 外部指定的认证方案
                                        logger.debug("HTTP动作[DELETE] 转发编号[" + inForwardInfo.getForwardCode() + "] 认证方式[" + specifyAuthType.name() + "]");
                                        switch (specifyAuthType) {
                                            case BASIC_AUTH: {
                                                break;
                                            }
                                            case DIGEST_AUTH: {
                                                rtString = HttpDigestRequestUtils.sendDelete(inForwardInfo.getForwardUrl(), "", specifyUsername, specifyPassword, new JSONObject(sendData).toJSONString(), "json");
                                                break;
                                            }
                                            case BEARER_TOKEN: {
                                                rtString = HttpUtils.sendJsonDeleteWithBearerToken(inForwardInfo.getForwardUrl(), sendData, specifyToken);
                                                break;
                                            }
                                            default:
                                                break;
                                        }
                                    } else {
                                        String authEnabled = inForwardInfo.getAuthEnabled();
                                        String authType = inForwardInfo.getAuthType();
                                        if (Constants.YES.equals(authEnabled)) {
                                            if (authType != null) {
                                                HttpAuthenticationType authTypeEnum = HttpAuthenticationType.valueOf(authType);
                                                if (authTypeEnum != null) {
                                                    logger.debug("HTTP动作[DELETE] 转发编号[" + inForwardInfo.getForwardCode() + "] 认证方式[" + authTypeEnum.name() + "]");
                                                    switch (authTypeEnum) {
                                                        case BASIC_AUTH: {
                                                            break;
                                                        }
                                                        case DIGEST_AUTH: {
                                                            // 获取用户名密码
                                                            String authSource = inForwardInfo.getAuthSource();
                                                            Map<String, String> usernamePasswordMap = getUsernamePassword(authSource);
                                                            String username = usernamePasswordMap.get(KEY_USERNAME);
                                                            String password = usernamePasswordMap.get(KEY_PASSWORD);
                                                            rtString = HttpDigestRequestUtils.sendDelete(inForwardInfo.getForwardUrl(), "", username, password, new JSONObject(sendData).toJSONString(), "json");
                                                            newInHttpAuthInfoVo = setSpecifyDigestAuth(username, password);
                                                            break;
                                                        }
                                                        case BEARER_TOKEN: {
                                                            rtString = HttpUtils.sendJsonDeleteWithBearerToken(inForwardInfo.getForwardUrl(), sendData, specifyToken);
                                                            newInHttpAuthInfoVo = setSpecifyBearerToken(specifyToken);
                                                            break;
                                                        }
                                                        default:
                                                            break;
                                                    }

                                                }
                                            }
                                        } else {
                                            rtString = HttpUtils.sendJsonDelete(inForwardInfo.getForwardUrl(), sendData);
                                        }
                                    }
                                    result = AjaxResult.success("转发编号["+inForwardInfo.getForwardCode()+"]转发成功", rtString);
                                } catch (Exception e) {
                                    String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]异常";
                                    logger.error(errMsg, e);
                                    result = AjaxResult.error(errMsg);
                                }
                            }
                        }
                    }
                        break;
                    case TCP:
                        break;
                    default:
                        break;
                }
            } else {

            }
        }
        logger.info("转发发送服务结束.");

        if (rtHttpAuthInfo) {
            result.put(KEY_IN_HTTP_AUTH_INFO, newInHttpAuthInfoVo);
        }

        return result;
    }

    private Map<String, String> getUsernamePassword(String appId) {
        Map<String, String> map = new HashMap<>();
        IInAppAccessService appAccessService = SpringUtils.getBean(IInAppAccessService.class);
        InAppAccess inAppAccess = appAccessService.selectInAppAccessById(appId);
        if (inAppAccess != null) {
            map.put(KEY_USERNAME, inAppAccess.getCertificationUsername());
            map.put(KEY_PASSWORD, inAppAccess.getCertificationPassword());
        }
        return map;
    }

    // 保存当前的认证信息，如果当前线程有下次请求（比如聚合服务的TCC确认或者取消请求）可以使用，请在触发器开启认证标志
    private InHttpAuthInfoVo setSpecifyDigestAuth(String username, String password) {
        InHttpAuthInfoVo newInHttpAuthInfoVo = new InHttpAuthInfoVo();
        BeanUtils.copyBeanProp(newInHttpAuthInfoVo, this.inHttpAuthInfoVo);
        newInHttpAuthInfoVo.setSpecifyAuthType(HttpAuthenticationType.DIGEST_AUTH);
        newInHttpAuthInfoVo.setSpecifyUsername(username);
        newInHttpAuthInfoVo.setSpecifyPassword(password);
        return newInHttpAuthInfoVo;
    }

    private InHttpAuthInfoVo setSpecifyBearerToken(String token) {
        InHttpAuthInfoVo newInHttpAuthInfoVo = new InHttpAuthInfoVo();
        BeanUtils.copyBeanProp(newInHttpAuthInfoVo, this.inHttpAuthInfoVo);
        newInHttpAuthInfoVo.setSpecifyAuthType(HttpAuthenticationType.BEARER_TOKEN);
        newInHttpAuthInfoVo.setSpecifyToken(token);
        return newInHttpAuthInfoVo;
    }

}
