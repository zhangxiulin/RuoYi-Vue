package com.ruoyi.integrator.controller;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/27 17:35
 */

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.HttpAuthenticationType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.domain.vo.InAggregateRequestVo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.domain.vo.InHttpAuthInfoVo;
import com.ruoyi.integrator.service.IInAggregateService;
import com.ruoyi.integrator.service.IInAggregationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @description: 聚合服务转发Controller
 * @author: zhangxiulin
 * @time: 2020/11/27 12:55
 */
@RestController
@RequestMapping("/integrator")
public class InAggregateController {

    private static final Logger log = LoggerFactory.getLogger(InAggregateController.class);

    @Autowired
    private IInAggregateService inAggregateService;

    @Autowired
    private TokenService tokenService;


    @PostMapping(value = "/aggr")
    public AjaxResult aggregate(@RequestBody JSONObject jsonObject) {
        InHttpAuthInfoVo inHttpAuthInfoVo = new InHttpAuthInfoVo();
        inHttpAuthInfoVo.setSpecifyEnabled(false);
        inHttpAuthInfoVo.setSpecifyAuthType(HttpAuthenticationType.BEARER_TOKEN);
        inHttpAuthInfoVo.setSpecifyToken(tokenService.getToken(ServletUtils.getRequest()));
        return inAggregateService.aggregate(jsonObject, inHttpAuthInfoVo);
    }

}
