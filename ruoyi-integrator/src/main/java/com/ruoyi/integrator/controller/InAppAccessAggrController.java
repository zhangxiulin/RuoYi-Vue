package com.ruoyi.integrator.controller;

import java.util.List;

import com.ruoyi.integrator.domain.vo.InAppAccessAggrVo;
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
import com.ruoyi.integrator.domain.InAppAccessAggr;
import com.ruoyi.integrator.service.IInAppAccessAggrService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 应用聚合权限Controller
 * 
 * @author zhangxiulin
 * @date 2021-02-07
 */
@RestController
@RequestMapping("/integrator/appAccessAggr")
public class InAppAccessAggrController extends BaseController
{
    @Autowired
    private IInAppAccessAggrService inAppAccessAggrService;

    /**
     * 查询应用聚合权限列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessAggr:list')")
    @GetMapping("/list")
    public TableDataInfo list(InAppAccessAggr inAppAccessAggr)
    {
        startPage();
        //List<InAppAccessAggr> list = inAppAccessAggrService.selectInAppAccessAggrList(inAppAccessAggr);
        List<InAppAccessAggrVo> list = inAppAccessAggrService.selectInAppAccessAggrVoList(inAppAccessAggr);
        return getDataTable(list);
    }

    /**
     * 导出应用聚合权限列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessAggr:export')")
    @Log(title = "应用聚合权限", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(InAppAccessAggr inAppAccessAggr)
    {
        List<InAppAccessAggr> list = inAppAccessAggrService.selectInAppAccessAggrList(inAppAccessAggr);
        ExcelUtil<InAppAccessAggr> util = new ExcelUtil<InAppAccessAggr>(InAppAccessAggr.class);
        return util.exportExcel(list, "appAccessAggr");
    }

    /**
     * 获取应用聚合权限详细信息
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessAggr:query')")
    @GetMapping(value = "/{appAggrId}")
    public AjaxResult getInfo(@PathVariable("appAggrId") String appAggrId)
    {
        return AjaxResult.success(inAppAccessAggrService.selectInAppAccessAggrById(appAggrId));
    }

    /**
     * 新增应用聚合权限
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessAggr:add')")
    @Log(title = "应用聚合权限", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InAppAccessAggr inAppAccessAggr)
    {
        return toAjax(inAppAccessAggrService.insertInAppAccessAggr(inAppAccessAggr));
    }

    /**
     * 修改应用聚合权限
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessAggr:edit')")
    @Log(title = "应用聚合权限", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InAppAccessAggr inAppAccessAggr)
    {
        return toAjax(inAppAccessAggrService.updateInAppAccessAggr(inAppAccessAggr));
    }

    /**
     * 删除应用聚合权限
     */
    @PreAuthorize("@ss.hasPermi('integrator:appAccessAggr:remove')")
    @Log(title = "应用聚合权限", businessType = BusinessType.DELETE)
	@DeleteMapping("/{appAggrIds}")
    public AjaxResult remove(@PathVariable String[] appAggrIds)
    {
        return toAjax(inAppAccessAggrService.deleteInAppAccessAggrByIds(appAggrIds));
    }
}
