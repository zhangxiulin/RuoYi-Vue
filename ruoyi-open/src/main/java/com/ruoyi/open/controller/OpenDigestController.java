package com.ruoyi.open.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.HttpAuthenticationType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.integrator.domain.vo.InHttpAuthInfoVo;
import com.ruoyi.integrator.service.IInAggregateService;
import com.ruoyi.integrator.service.IInForwardService;
import com.ruoyi.open.annotation.OpenDigestCheck;
import com.ruoyi.open.service.IOpenDigestService;
import com.ruoyi.open.service.IOpenLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: http digest 认证方式接入
 *
 * 客户端可能需要先发送Get请求获得Digest的约定的认证信息，认证信息位于响应头中，请求方法的入参建议留空，返回值可以为void或AjaxResult
 *
 * @author: zhangxiulin
 * @time: 2021/2/3 12:49
 */
@RestController
@RequestMapping("/open/digest")
public class OpenDigestController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(OpenDigestController.class);

    private static final String KEY_REQ_ID = "reqId";

    @Autowired
    private IInForwardService inForwardService;

    @Autowired
    private IInAggregateService inAggregateService;

    @Autowired
    private IOpenLimitService limitService;

    @PostMapping(value = "/forward")
    @OpenDigestCheck
    public AjaxResult forward(@RequestBody JSONObject jsonObject)
    {
        AjaxResult ajaxResult = null;
        // 判断转发权限
        boolean forwardLimit = limitService.forwardLimit(ServletUtils.getRequest(), ServletUtils.getResponse(), jsonObject.getString(KEY_REQ_ID));
        if (forwardLimit) {
            ajaxResult = inForwardService.forward(jsonObject);
        } else {
            ajaxResult = new AjaxResult(HttpStatus.FORBIDDEN, "无转发权限");
        }
        return ajaxResult;
    }

    /**
     *@Description: 客户端可能需要先发送Get请求获得Digest的约定，此方法将必要的Digest信息放入响应头中
     *@params: 无
     *@return: 无
     */
    @GetMapping(value = "/forward")
    @OpenDigestCheck
    public void forward(){}

    @PostMapping(value = "/aggr")
    @OpenDigestCheck
    public AjaxResult aggregate(@RequestBody JSONObject jsonObject)
    {
        AjaxResult ajaxResult = null;
        InHttpAuthInfoVo inHttpAuthInfoVo = new InHttpAuthInfoVo();
        inHttpAuthInfoVo.setSpecifyEnabled(false);
        // 判断聚合权限
        boolean aggrLimit = limitService.aggrLimit(ServletUtils.getRequest(), ServletUtils.getResponse(), jsonObject.getString(KEY_REQ_ID));
        if (aggrLimit) {
            ajaxResult = inAggregateService.aggregate(jsonObject, inHttpAuthInfoVo);
        } else {
            ajaxResult = new AjaxResult(HttpStatus.FORBIDDEN, "无聚合服务权限");
        }
        return ajaxResult;
    }

    @GetMapping(value = "/aggr")
    @OpenDigestCheck
    public void aggregate(){}

    @GetMapping(value = "/testVoid")
    @OpenDigestCheck
    public void testVoid(){}

    @GetMapping(value = "/testInt")
    @OpenDigestCheck
    public int testInt(){return 0;}

    @GetMapping(value = "/testString")
    @OpenDigestCheck
    public String testString(){return "啦啦啦，Digest信息请看响应头";}

    @GetMapping(value = "/testString2")
    @OpenDigestCheck(rtType = String.class, rtErrValue = "嘿嘿嘿，Digest信息请看响应头")
    public String testString2(){return "啦啦啦，Digest信息请看响应头";}

    @GetMapping(value = "/testInt2")
    @OpenDigestCheck(rtType = int.class, rtErrValue = "999999")
    public int testInt2(){return 0;}

    @GetMapping(value = "/testAjaxResult")
    @OpenDigestCheck
    public AjaxResult testAjaxResult(){return null;}

    @GetMapping(value = "/testAjaxResult2")
    @OpenDigestCheck(rtType = AjaxResult.class, rtErrValue = "{\"tip\":\"Digest信息请看响应头\"}")
    public AjaxResult testAjaxResult2(){return null;}

}
