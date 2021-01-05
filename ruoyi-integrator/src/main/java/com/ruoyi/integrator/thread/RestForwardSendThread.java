package com.ruoyi.integrator.thread;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils2;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.enums.InForwardMethod;
import com.ruoyi.integrator.enums.InForwardProtocol;
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

    public RestForwardSendThread(InForwardInfo inForwardInfo, Map<String, Object> sendVar, Map<String, Object> sendData){
        this.inForwardInfo = inForwardInfo;
        this.sendVar = sendVar;
        this.sendData = sendData;
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
                        logger.info("转发协议[HTTP] 转发编号[" + inForwardInfo.getForwardCode() + "]");
                        if (StringUtils.isNotEmpty(inForwardInfo.getForwardMethod())){
                            InForwardMethod forwardMethodEnum = InForwardMethod.valueOf(inForwardInfo.getForwardMethod());
                            if (InForwardMethod.GET == forwardMethodEnum){
                                logger.debug("HTTP动作[GET] 转发编号[" + inForwardInfo.getForwardCode() + "]");
                                try {
                                    String rtString = HttpUtils2.sendGet(inForwardInfo.getForwardUrl(), sendData);
                                    result = AjaxResult.success("转发编号["+inForwardInfo.getForwardCode()+"]转发成功", rtString);
                                } catch (Exception e) {
                                    String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]异常";
                                    logger.error(errMsg, e);
                                    result = AjaxResult.error(errMsg);
                                }
                            } else if (InForwardMethod.POST == forwardMethodEnum){
                                logger.debug("HTTP动作[POST] 转发编号[" + inForwardInfo.getForwardCode() + "]");
                                try {
                                    String rtString = HttpUtils2.sendJsonPost(inForwardInfo.getForwardUrl(), sendData);
                                    result = AjaxResult.success("转发编号["+inForwardInfo.getForwardCode()+"]转发成功", rtString);
                                } catch (Exception e) {
                                    String errMsg = "转发编号[" + inForwardInfo.getForwardCode() + "]异常";
                                    logger.error(errMsg, e);
                                    result = AjaxResult.error(errMsg);
                                }
                            } else if (InForwardMethod.PUT == forwardMethodEnum) {
                                logger.debug("HTTP动作[PUT] 转发编号[" + inForwardInfo.getForwardCode() + "]");
                            } else if (InForwardMethod.DELETE == forwardMethodEnum) {
                                logger.debug("HTTP动作[DELETE] 转发编号["  + inForwardInfo.getForwardCode() + "]");
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
        return result;
    }

}
