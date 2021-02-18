package com.ruoyi.demo.open.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.demo.domain.vo.DemoAccountWaterTccTryVo;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccCancelVo;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccConfirmVo;
import com.ruoyi.demo.enums.JieDai;
import com.ruoyi.demo.service.IDemoAccountWaterTccService;
import com.ruoyi.open.annotation.OpenDigestCheck;
import com.ruoyi.open.annotation.OpenLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: TCC动账流水开放接口
 * @author: zhangxiulin
 * @time: 2021/2/9 22:54
 */
@RestController
@RequestMapping("/open/demo/accountWaterTcc")
public class OpenDemoAccountWaterTccController extends BaseController {

    @Autowired
    private IDemoAccountWaterTccService demoAccountWaterTccService;

    @OpenLog(title = "try阶段预留额度(收入)", businessType = BusinessType.INSERT)
    @PostMapping("/tryIncome")
    @OpenDigestCheck
    public AjaxResult tryIncome(@RequestBody DemoAccountWaterTccTryVo tryVo)
    {
        tryVo.setJdFlag(JieDai.J.name());
        return demoAccountWaterTccService.tryReserveMoney(tryVo);
    }

    @OpenLog(title = "try阶段预留额度(收入)", businessType = BusinessType.INSERT)
    @GetMapping("/tryIncome")
    @OpenDigestCheck
    public void tryIncome(){}

    @OpenLog(title = "try阶段预留额度(支付)", businessType = BusinessType.INSERT)
    @PostMapping("/tryPay")
    @OpenDigestCheck
    public AjaxResult tryPay(@RequestBody DemoAccountWaterTccTryVo tryVo)
    {
        tryVo.setJdFlag(JieDai.D.name());
        return demoAccountWaterTccService.tryReserveMoney(tryVo);
    }

    @OpenLog(title = "try阶段预留额度(支付)", businessType = BusinessType.INSERT)
    @GetMapping("/tryPay")
    @OpenDigestCheck
    public AjaxResult tryPay()
    {
        return AjaxResult.success();
    }

    @OpenLog(title = "confirm阶段确认转账", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm")
    @OpenDigestCheck
    public AjaxResult confirm(@RequestBody DemoTransferMoneyTccConfirmVo confirmVo)
    {
        return demoAccountWaterTccService.confirmMoney(confirmVo);
    }

    @OpenLog(title = "confirm阶段确认转账", businessType = BusinessType.UPDATE)
    @GetMapping("/confirm")
    @OpenDigestCheck
    public void confirm(){}

    @OpenLog(title = "cancel阶段取消转账", businessType = BusinessType.UPDATE)
    @DeleteMapping("/cancel")
    @OpenDigestCheck
    public AjaxResult cancel(@RequestBody DemoTransferMoneyTccCancelVo cancelVo)
    {
        return demoAccountWaterTccService.cancelMoney(cancelVo);
    }

    @OpenLog(title = "cancel阶段取消转账", businessType = BusinessType.UPDATE)
    @GetMapping("/cancel")
    @OpenDigestCheck
    public void cancel() { }



}
