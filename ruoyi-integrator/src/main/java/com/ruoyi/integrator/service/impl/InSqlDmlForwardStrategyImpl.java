package com.ruoyi.integrator.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInForwardStrategy;
import com.ruoyi.integrator.thread.SqlDmlForwardSendThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @description: SQL增、删、改
 * @author: zhangxiulin
 * @time: 2020/12/8 10:16
 */
@Component
public class InSqlDmlForwardStrategyImpl implements IInForwardStrategy {

    private static final Logger log = LoggerFactory.getLogger(InSqlDmlForwardStrategyImpl.class);

    @Override
    public InForwardType getType() {
        return InForwardType.SQL_DML;
    }

    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public Object forward(InForwardRequestVo request) {
        log.info("本次请求[" + request.getReqId() + "]为DB操纵请求");
        AjaxResult ajaxResult = null;
        InForwardInfo inForwardInfo = request.getInForwardInfo();
        SqlDmlForwardSendThread sdfst = new SqlDmlForwardSendThread(inForwardInfo, request.getVar(), request.getData(), request.getDataList());
        if (Constants.YES.equals(inForwardInfo.getIsAsync())){ // 异步
            threadPoolTaskExecutor.submit(sdfst);
            ajaxResult = AjaxResult.success("转发提交成功");
        } else { // 同步
            ajaxResult = sdfst.call();
        }
        return ajaxResult;
    }
}
