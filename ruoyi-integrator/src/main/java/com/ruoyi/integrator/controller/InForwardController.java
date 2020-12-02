package com.ruoyi.integrator.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 转发Controller
 * @author: zhangxiulin
 * @time: 2020/11/27 12:55
 */
@RestController
@RequestMapping("/integrator/forward")
public class InForwardController extends BaseController {

    @PreAuthorize("@ss.hasPermi('integrator:forward:normal')")
    @PostMapping(value = "/normal")
    public AjaxResult normal(@RequestBody InForwardRequestVo requestVo)
    {

        //return AjaxResult.success(inForwardInfoService.selectInForwardInfoById(infoId));
        return null;
    }

}
