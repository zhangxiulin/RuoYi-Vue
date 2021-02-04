package com.ruoyi.integrator.controller;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInForwardInfoService;
import com.ruoyi.integrator.service.IInForwardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 转发Controller
 * @author: zhangxiulin
 * @time: 2020/11/27 12:55
 */
@RestController
@RequestMapping("/integrator")
public class InForwardController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(InForwardController.class);

    @Autowired
    private IInForwardService inForwardService;

    //@PreAuthorize("@ss.hasPermi('integrator:forward:normal')")
    @PostMapping(value = "/forward")
    public AjaxResult forward(@RequestBody JSONObject jsonObject)
    {
        return inForwardService.forward(jsonObject);
    }

}
