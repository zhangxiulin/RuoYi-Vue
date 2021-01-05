package com.ruoyi.integrator.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInForwardStrategy;
import com.ruoyi.integrator.thread.RestForwardSendThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @description: 转发策略Rest实现
 * @author: zhangxiulin
 * @time: 2020/12/8 10:16
 */
@Component
public class InRestForwardStrategy implements IInForwardStrategy {

    private static final Logger logger = LoggerFactory.getLogger(InRestForwardStrategy.class);

    @Override
    public InForwardType getType() {
        return InForwardType.REST;
    }

    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public Object forward(InForwardRequestVo request) {
        logger.info("本次请求[" + request.getReqId() + "]为HTTP转发请求");
        AjaxResult ajaxResult = null;
        InForwardInfo inForwardInfo = request.getInForwardInfo();
        RestForwardSendThread rfst = new RestForwardSendThread(inForwardInfo, request.getVar(), request.getData());
        if (Constants.YES.equals(inForwardInfo.getIsAsync())){ // 异步
            threadPoolTaskExecutor.submit(rfst);
            ajaxResult = AjaxResult.success("转发提交成功");
        } else { // 同步
            ajaxResult = rfst.call();
        }
        return ajaxResult;
    }
}
