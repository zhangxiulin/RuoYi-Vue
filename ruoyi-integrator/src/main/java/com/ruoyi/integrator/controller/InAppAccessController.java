package com.ruoyi.integrator.controller;

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
import com.ruoyi.integrator.domain.InAppAccess;
import com.ruoyi.integrator.service.IInAppAccessService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 接入应用Controller
 * 
 * @author zhangxiulin
 * @date 2021-02-01
 */
@RestController
@RequestMapping("/integrator/appAccess")
public class InAppAccessController extends BaseController
{
    @Autowired
    private IInAppAccessService inAppAccessService;

    /**
     * 查询接入应用列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccess:list')")
    @GetMapping("/list")
    public TableDataInfo list(InAppAccess inAppAccess)
    {
        startPage();
        List<InAppAccess> list = inAppAccessService.selectInAppAccessList(inAppAccess);
        return getDataTable(list);
    }

    /**
     * 导出接入应用列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccess:export')")
    @Log(title = "接入应用", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(InAppAccess inAppAccess)
    {
        List<InAppAccess> list = inAppAccessService.selectInAppAccessList(inAppAccess);
        ExcelUtil<InAppAccess> util = new ExcelUtil<InAppAccess>(InAppAccess.class);
        return util.exportExcel(list, "appAccess");
    }

    /**
     * 获取接入应用详细信息
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccess:query')")
    @GetMapping(value = "/{appId}")
    public AjaxResult getInfo(@PathVariable("appId") String appId)
    {
        return AjaxResult.success(inAppAccessService.selectInAppAccessById(appId));
    }

    /**
     * 新增接入应用
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccess:add')")
    @Log(title = "接入应用", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InAppAccess inAppAccess)
    {
        return toAjax(inAppAccessService.insertInAppAccess(inAppAccess));
    }

    /**
     * 修改接入应用
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccess:edit')")
    @Log(title = "接入应用", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InAppAccess inAppAccess)
    {
        return toAjax(inAppAccessService.updateInAppAccess(inAppAccess));
    }

    /**
     * 删除接入应用
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccess:remove')")
    @Log(title = "接入应用", businessType = BusinessType.DELETE)
	@DeleteMapping("/{appIds}")
    public AjaxResult remove(@PathVariable String[] appIds)
    {
        return toAjax(inAppAccessService.deleteInAppAccessByIds(appIds));
    }
}
