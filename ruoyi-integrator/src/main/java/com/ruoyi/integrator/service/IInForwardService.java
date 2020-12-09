package com.ruoyi.integrator.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;

/**
 * 转发Service接口
 *
 * @author zhangxiulin
 * @date 2020-11-26
 */
public interface IInForwardService {

    AjaxResult forward(InForwardType type, InForwardRequestVo request);

}
