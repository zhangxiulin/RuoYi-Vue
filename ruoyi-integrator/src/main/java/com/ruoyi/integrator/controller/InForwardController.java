package com.ruoyi.integrator.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.domain.vo.InForwardRequestVo;
import com.ruoyi.integrator.enums.InForwardType;
import com.ruoyi.integrator.service.IInForwardInfoService;
import com.ruoyi.integrator.service.IInForwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 转发Controller
 * @author: zhangxiulin
 * @time: 2020/11/27 12:55
 */
@RestController
@RequestMapping("/integrator")
public class InForwardController extends BaseController {

    @Autowired
    private IInForwardService inForwardService;

    @Autowired
    private IInForwardInfoService inForwardInfoService;

    //@PreAuthorize("@ss.hasPermi('integrator:forward:normal')")
    @PostMapping(value = "/forward")
    public AjaxResult forward(@Validated @RequestBody InForwardRequestVo inForwardRequestVo)
    {
        AjaxResult ajaxResult = null;
        // 根据转发编号获取转发配置信息
        InForwardInfo inForwardInfo = inForwardInfoService.selectInForwardInfoByCode(inForwardRequestVo.getReqId());
        if (inForwardInfo != null){
            inForwardRequestVo.setInForwardInfo(inForwardInfo);
            InForwardType inForwardType = InForwardType.getEnumByCode(inForwardInfo.getForwardType());
            if (inForwardType != null){
                ajaxResult = inForwardService.forward(inForwardType, inForwardRequestVo);
                if (ajaxResult == null){
                    ajaxResult = AjaxResult.error("转发器返回null");
                }
            }else{
                ajaxResult = AjaxResult.error("转发配置信息不正确");
            }
        } else{
            ajaxResult = AjaxResult.error("未找到转发配置信息");
        }

        return ajaxResult;
    }

}
