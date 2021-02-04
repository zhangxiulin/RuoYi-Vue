package com.ruoyi.open.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.service.IInAggregateService;
import com.ruoyi.integrator.service.IInForwardService;
import com.ruoyi.open.service.IDigestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: http digest 认证方式接入
 * @author: zhangxiulin
 * @time: 2021/2/3 12:49
 */
@RestController
@RequestMapping("/open/digest")
public class DigestController extends BaseController {


    @Autowired
    private IDigestService digestService;

    @Autowired
    private IInForwardService inForwardService;

    @Autowired
    private IInAggregateService inAggregateService;

    @PostMapping(value = "/forward")
    public AjaxResult forward(@RequestBody JSONObject jsonObject)
    {
        AjaxResult ajaxResult = null;
        int authRt = digestService.auth(this.getHttpServletRequest(), this.getHttpServletResponse());
        if (authRt == HttpStatus.SUCCESS) {
            ajaxResult = inForwardService.forward(jsonObject);
        } else {
            ajaxResult = new AjaxResult(authRt, "认证失败");
        }
        return ajaxResult;
    }

    @PostMapping(value = "/aggr")
    public AjaxResult aggregate(@RequestBody JSONObject jsonObject)
    {
        AjaxResult ajaxResult = null;
        int authRt = digestService.auth(this.getHttpServletRequest(), this.getHttpServletResponse());
        if (authRt == HttpStatus.SUCCESS) {
            ajaxResult = inAggregateService.aggregate(jsonObject);
        } else {
            ajaxResult = new AjaxResult(authRt, "认证失败");
        }
        return ajaxResult;
    }

    private HttpServletRequest getHttpServletRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    private HttpServletResponse getHttpServletResponse(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getResponse();
    }
}
