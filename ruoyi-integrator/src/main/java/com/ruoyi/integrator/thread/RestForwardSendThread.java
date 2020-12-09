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

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/8 12:42
 */
public class RestForwardSendThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RestForwardSendThread.class);

    private InForwardInfo inForwardInfo;

    private Map<String, Object> sendVar;

    private Map<String, Object> sendData;

    private Object result;

    public RestForwardSendThread(InForwardInfo inForwardInfo, Map<String, Object> sendVar, Map<String, Object> sendData){
        this.inForwardInfo = inForwardInfo;
        this.sendVar = sendVar;
        this.sendData = sendData;
    }

    @Override
    public void run() {
        logger.info("开始处理转发发送服务...");
        if (inForwardInfo != null){
            // 区分协议，目前只支持http
            if (StringUtils.isNotEmpty(inForwardInfo.getForwardProtocol())){
                InForwardProtocol forwardProtocolEnum = InForwardProtocol.valueOf(inForwardInfo.getForwardProtocol());
                switch (forwardProtocolEnum){
                    case HTTP:
                    {
                        logger.info("转发协议[HTTP]");
                        if (StringUtils.isNotEmpty(inForwardInfo.getForwardMethod())){
                            InForwardMethod forwardMethodEnum = InForwardMethod.valueOf(inForwardInfo.getForwardMethod());
                            if (InForwardMethod.GET == forwardMethodEnum){
                                logger.debug("HTTP动作[GET]");
                                try {
                                    String rtString = HttpUtils2.sendGet(inForwardInfo.getForwardUrl(), sendData);
                                    result = AjaxResult.success("转发成功", rtString);
                                } catch (Exception e) {
                                    logger.error("转发编号[" + inForwardInfo.getForwardCode() + "]异常", e);
                                    result = AjaxResult.error("转发异常");
                                }
                            } else if (InForwardMethod.POST == forwardMethodEnum){
                                logger.debug("HTTP动作[POST]");
                                try {
                                    String rtString = HttpUtils2.sendJsonPost(inForwardInfo.getForwardUrl(), sendData);
                                    result = AjaxResult.success("转发成功", rtString);
                                } catch (Exception e) {
                                    logger.error("转发编号[" + inForwardInfo.getForwardCode() + "]异常", e);
                                    result = AjaxResult.error("转发异常");
                                }
                            } else if (InForwardMethod.PUT == forwardMethodEnum) {
                                logger.debug("HTTP动作[PUT]");
                            } else if (InForwardMethod.DELETE == forwardMethodEnum) {
                                logger.debug("HTTP动作[DELETE]");
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
    }

    public Object getResult() {
        return result;
    }
}
