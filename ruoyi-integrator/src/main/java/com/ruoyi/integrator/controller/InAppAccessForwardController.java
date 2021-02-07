package com.ruoyi.integrator.controller;

import java.util.List;

import com.ruoyi.integrator.domain.vo.InAppAccessForwardVo;
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
import com.ruoyi.integrator.domain.InAppAccessForward;
import com.ruoyi.integrator.service.IInAppAccessForwardService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 应用转发权限Controller
 * 
 * @author zhangxiulin
 * @date 2021-02-04
 */
@RestController
@RequestMapping("/integrator/appAccessForward")
public class InAppAccessForwardController extends BaseController
{
    @Autowired
    private IInAppAccessForwardService inAppAccessForwardService;

    /**
     * 查询应用转发权限列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessForward:list')")
    @GetMapping("/list")
    public TableDataInfo list(InAppAccessForward inAppAccessForward)
    {
        startPage();
        //List<InAppAccessForward> list = inAppAccessForwardService.selectInAppAccessForwardList(inAppAccessForward);
        List<InAppAccessForwardVo> list = inAppAccessForwardService.selectInAppAccessForwardVoList(inAppAccessForward);
        return getDataTable(list);
    }

    /**
     * 导出应用转发权限列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessForward:export')")
    @Log(title = "应用转发权限", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(InAppAccessForward inAppAccessForward)
    {
        List<InAppAccessForward> list = inAppAccessForwardService.selectInAppAccessForwardList(inAppAccessForward);
        ExcelUtil<InAppAccessForward> util = new ExcelUtil<InAppAccessForward>(InAppAccessForward.class);
        return util.exportExcel(list, "appAccessForward");
    }

    /**
     * 获取应用转发权限详细信息
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessForward:query')")
    @GetMapping(value = "/{appFwdId}")
    public AjaxResult getInfo(@PathVariable("appFwdId") String appFwdId)
    {
        return AjaxResult.success(inAppAccessForwardService.selectInAppAccessForwardById(appFwdId));
    }

    /**
     * 新增应用转发权限
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessForward:add')")
    @Log(title = "应用转发权限", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InAppAccessForward inAppAccessForward)
    {
        return toAjax(inAppAccessForwardService.insertInAppAccessForward(inAppAccessForward));
    }

    /**
     * 修改应用转发权限
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessForward:edit')")
    @Log(title = "应用转发权限", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InAppAccessForward inAppAccessForward)
    {
        return toAjax(inAppAccessForwardService.updateInAppAccessForward(inAppAccessForward));
    }

    /**
     * 删除应用转发权限
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessForward:remove')")
    @Log(title = "应用转发权限", businessType = BusinessType.DELETE)
	@DeleteMapping("/{appFwdIds}")
    public AjaxResult remove(@PathVariable String[] appFwdIds)
    {
        return toAjax(inAppAccessForwardService.deleteInAppAccessForwardByIds(appFwdIds));
    }
}
