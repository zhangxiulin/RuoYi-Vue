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
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.service.IInForwardInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 转发配置Controller
 * 
 * @author zhangxiulin
 * @date 2020-12-02
 */
@RestController
@RequestMapping("/integrator/forwardInfo")
public class InForwardInfoController extends BaseController
{
    @Autowired
    private IInForwardInfoService inForwardInfoService;

    /**
     * 查询转发配置列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:forwardInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(InForwardInfo inForwardInfo)
    {
        startPage();
        List<InForwardInfo> list = inForwardInfoService.selectInForwardInfoList(inForwardInfo);
        return getDataTable(list);
    }

    /**
     * 导出转发配置列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:forwardInfo:export')")
    @Log(title = "转发配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(InForwardInfo inForwardInfo)
    {
        List<InForwardInfo> list = inForwardInfoService.selectInForwardInfoList(inForwardInfo);
        ExcelUtil<InForwardInfo> util = new ExcelUtil<InForwardInfo>(InForwardInfo.class);
        return util.exportExcel(list, "forwardInfo");
    }

    /**
     * 获取转发配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('integrator:forwardInfo:query')")
    @GetMapping(value = "/{infoId}")
    public AjaxResult getInfo(@PathVariable("infoId") String infoId)
    {
        return AjaxResult.success(inForwardInfoService.selectInForwardInfoById(infoId));
    }

    /**
     * 新增转发配置
     */
    @PreAuthorize("@ss.hasPermi('integrator:forwardInfo:add')")
    @Log(title = "转发配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InForwardInfo inForwardInfo)
    {
        return toAjax(inForwardInfoService.insertInForwardInfo(inForwardInfo));
    }

    /**
     * 修改转发配置
     */
    @PreAuthorize("@ss.hasPermi('integrator:forwardInfo:edit')")
    @Log(title = "转发配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InForwardInfo inForwardInfo)
    {
        return toAjax(inForwardInfoService.updateInForwardInfo(inForwardInfo));
    }

    /**
     * 删除转发配置
     */
    @PreAuthorize("@ss.hasPermi('integrator:forwardInfo:remove')")
    @Log(title = "转发配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable String[] infoIds)
    {
        return toAjax(inForwardInfoService.deleteInForwardInfoByIds(infoIds));
    }
}
