package com.ruoyi.integrator.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInForwardService;
import com.ruoyi.integrator.service.IInForwardStrategy;
import com.ruoyi.integrator.service.InForwardContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 转发
 * @author: zhangxiulin
 * @time: 2020/11/27 13:28
 */
@Service
public class InForwardServiceImpl implements IInForwardService {

    @Autowired
    private InForwardContext inForwardContext;

    @Override
    public AjaxResult forward(InForwardType type, InForwardRequestVo request  ) {
        return inForwardContext.forward(type, request);
    }

}
