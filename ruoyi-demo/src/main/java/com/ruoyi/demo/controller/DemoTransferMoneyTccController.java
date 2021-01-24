package com.ruoyi.demo.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccCancelVo;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccConfirmVo;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccTryVo;
import com.ruoyi.demo.service.IDemoAccountWaterTccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: TCC 方案转账
 * @author: zhangxiulin
 * @time: 2021/1/21 12:42
 */
@RestController
@RequestMapping("/demo/transferMoneyTcc")
public class DemoTransferMoneyTccController {

    @Autowired
    private IDemoAccountWaterTccService demoAccountWaterTccService;

    @PreAuthorize("@ss.hasPermi('demo:transferMoneyTcc:tryReserve')")
    @Log(title = "try阶段预留额度", businessType = BusinessType.INSERT)
    @PostMapping("/tryReserve")
    public AjaxResult tryReserve(@RequestBody DemoTransferMoneyTccTryVo tryVo)
    {
        //return toAjax(demoAccountService.insertDemoAccount(demoAccount));
        return null;
    }

    @PreAuthorize("@ss.hasPermi('demo:transferMoneyTcc:confirm')")
    @Log(title = "confirm阶段确认转账", businessType = BusinessType.UPDATE)
    @PostMapping("/confirm")
    public AjaxResult confirm(@RequestBody DemoTransferMoneyTccConfirmVo confirmVo)
    {
        //return toAjax(demoAccountService.insertDemoAccount(demoAccount));
        return null;
    }

    @PreAuthorize("@ss.hasPermi('demo:transferMoneyTcc:cancel')")
    @Log(title = "cancel阶段取消转账", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel")
    public AjaxResult cancel(@RequestBody DemoTransferMoneyTccCancelVo cancelVo)
    {
        //return toAjax(demoAccountService.insertDemoAccount(demoAccount));
        return null;
    }

}
