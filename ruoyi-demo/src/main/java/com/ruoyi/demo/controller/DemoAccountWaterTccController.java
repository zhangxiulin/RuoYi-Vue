package com.ruoyi.demo.controller;

import java.util.List;

import com.ruoyi.demo.domain.vo.DemoAccountWaterTccTryVo;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccCancelVo;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccConfirmVo;
import com.ruoyi.demo.enums.JieDai;
import org.apache.ibatis.annotations.Delete;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.demo.domain.DemoAccountWaterTcc;
import com.ruoyi.demo.service.IDemoAccountWaterTccService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * TCC动账流水Controller
 * 
 * @author zhangxiulin
 * @date 2021-01-20
 */
@RestController
@RequestMapping("/demo/accountWaterTcc")
public class DemoAccountWaterTccController extends BaseController
{
    @Autowired
    private IDemoAccountWaterTccService demoAccountWaterTccService;

    /**
     * 查询TCC动账流水列表
     */
    @PreAuthorize("@ss.hasPermi('demo:accountWaterTcc:list')")
    @GetMapping("/list")
    public TableDataInfo list(DemoAccountWaterTcc demoAccountWaterTcc)
    {
        startPage();
        List<DemoAccountWaterTcc> list = demoAccountWaterTccService.selectDemoAccountWaterTccList(demoAccountWaterTcc);
        return getDataTable(list);
    }

    /**
     * 导出TCC动账流水列表
     */
    @PreAuthorize("@ss.hasPermi('demo:accountWaterTcc:export')")
    @Log(title = "TCC动账流水", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DemoAccountWaterTcc demoAccountWaterTcc)
    {
        List<DemoAccountWaterTcc> list = demoAccountWaterTccService.selectDemoAccountWaterTccList(demoAccountWaterTcc);
        ExcelUtil<DemoAccountWaterTcc> util = new ExcelUtil<DemoAccountWaterTcc>(DemoAccountWaterTcc.class);
        return util.exportExcel(list, "accountWaterTcc");
    }

    /**
     * 获取TCC动账流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('demo:accountWaterTcc:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(demoAccountWaterTccService.selectDemoAccountWaterTccById(id));
    }

    /**
     * 新增TCC动账流水
     */
    @PreAuthorize("@ss.hasPermi('demo:accountWaterTcc:add')")
    @Log(title = "TCC动账流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DemoAccountWaterTcc demoAccountWaterTcc)
    {
        return toAjax(demoAccountWaterTccService.insertDemoAccountWaterTcc(demoAccountWaterTcc));
    }

    /**
     * 修改TCC动账流水
     */
    @PreAuthorize("@ss.hasPermi('demo:accountWaterTcc:edit')")
    @Log(title = "TCC动账流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DemoAccountWaterTcc demoAccountWaterTcc)
    {
        return toAjax(demoAccountWaterTccService.updateDemoAccountWaterTcc(demoAccountWaterTcc));
    }

    /**
     * 删除TCC动账流水
     */
    @PreAuthorize("@ss.hasPermi('demo:accountWaterTcc:remove')")
    @Log(title = "TCC动账流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(demoAccountWaterTccService.deleteDemoAccountWaterTccByIds(ids));
    }

    //@PreAuthorize("@ss.hasPermi('demo:accountWaterTcc:tryReserve')")
    @Log(title = "try阶段预留额度(收入)", businessType = BusinessType.INSERT)
    @PostMapping("/tryIncome")
    public AjaxResult tryIncome(@RequestBody DemoAccountWaterTccTryVo tryVo)
    {
        tryVo.setJdFlag(JieDai.J.name());
        return demoAccountWaterTccService.tryReserveMoney(tryVo);
    }

    @Log(title = "try阶段预留额度(支付)", businessType = BusinessType.INSERT)
    @PostMapping("/tryPay")
    public AjaxResult tryPay(@RequestBody DemoAccountWaterTccTryVo tryVo)
    {
        tryVo.setJdFlag(JieDai.D.name());
        return demoAccountWaterTccService.tryReserveMoney(tryVo);
    }

    //@PreAuthorize("@ss.hasPermi('demo:accountWaterTcc:confirm')")
    @Log(title = "confirm阶段确认转账", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm")
    public AjaxResult confirm(@RequestBody DemoTransferMoneyTccConfirmVo confirmVo)
    {
        return demoAccountWaterTccService.confirmMoney(confirmVo);
    }

    //@PreAuthorize("@ss.hasPermi('demo:accountWaterTcc:cancel')")
    @Log(title = "cancel阶段取消转账", businessType = BusinessType.UPDATE)
    @DeleteMapping("/cancel")
    public AjaxResult cancel(@RequestBody DemoTransferMoneyTccCancelVo cancelVo)
    {
        return demoAccountWaterTccService.cancelMoney(cancelVo);
    }


}
