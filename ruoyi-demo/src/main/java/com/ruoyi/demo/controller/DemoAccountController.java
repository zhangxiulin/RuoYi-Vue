package com.ruoyi.demo.controller;

import java.util.List;
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
import com.ruoyi.demo.domain.DemoAccount;
import com.ruoyi.demo.service.IDemoAccountService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 账户Controller
 * 
 * @author zhangxiulin
 * @date 2021-01-20
 */
@RestController
@RequestMapping("/demo/account")
public class DemoAccountController extends BaseController
{
    @Autowired
    private IDemoAccountService demoAccountService;

    /**
     * 查询账户列表
     */
    @PreAuthorize("@ss.hasPermi('demo:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(DemoAccount demoAccount)
    {
        startPage();
        List<DemoAccount> list = demoAccountService.selectDemoAccountList(demoAccount);
        return getDataTable(list);
    }

    /**
     * 导出账户列表
     */
    @PreAuthorize("@ss.hasPermi('demo:account:export')")
    @Log(title = "账户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DemoAccount demoAccount)
    {
        List<DemoAccount> list = demoAccountService.selectDemoAccountList(demoAccount);
        ExcelUtil<DemoAccount> util = new ExcelUtil<DemoAccount>(DemoAccount.class);
        return util.exportExcel(list, "account");
    }

    /**
     * 获取账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('demo:account:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(demoAccountService.selectDemoAccountById(id));
    }

    /**
     * 新增账户
     */
    @PreAuthorize("@ss.hasPermi('demo:account:add')")
    @Log(title = "账户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DemoAccount demoAccount)
    {
        return toAjax(demoAccountService.insertDemoAccount(demoAccount));
    }

    /**
     * 修改账户
     */
    @PreAuthorize("@ss.hasPermi('demo:account:edit')")
    @Log(title = "账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DemoAccount demoAccount)
    {
        return toAjax(demoAccountService.updateDemoAccount(demoAccount));
    }

    /**
     * 删除账户
     */
    @PreAuthorize("@ss.hasPermi('demo:account:remove')")
    @Log(title = "账户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(demoAccountService.deleteDemoAccountByIds(ids));
    }
}
