package com.ruoyi.integrator.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInForwardStrategy;
import org.springframework.stereotype.Component;

/**
 * @description: SQL增、删、改
 * @author: zhangxiulin
 * @time: 2020/12/8 10:16
 */
@Component
public class InSqlDmlForwardStrategy implements IInForwardStrategy {

    @Override
    public InForwardType getType() {
        return InForwardType.SQL_DML;
    }

    @Override
    public Object forward(InForwardRequestVo request) {
        return null;
    }
}
