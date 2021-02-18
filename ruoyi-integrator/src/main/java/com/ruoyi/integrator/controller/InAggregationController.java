package com.ruoyi.integrator.controller;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.integrator.service.IInAggregationForwardService;
import com.ruoyi.integrator.service.IInForwardInfoService;
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
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.service.IInAggregationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 服务聚合Controller
 * 
 * @author zhangxiulin
 * @date 2020-12-21
 */
@RestController
@RequestMapping("/integrator/aggregation")
public class InAggregationController extends BaseController
{
    @Autowired
    private IInAggregationService inAggregationService;

    @Autowired
    private IInForwardInfoService inForwardInfoService;

    @Autowired
    private IInAggregationForwardService inAggregationForwardService;

    /**
     * 查询服务聚合列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:aggregation:list')")
    @GetMapping("/list")
    public TableDataInfo list(InAggregation inAggregation)
    {
        startPage();
        List<InAggregation> list = inAggregationService.selectInAggregationList(inAggregation);
        return getDataTable(list);
    }

    /**
     * 导出服务聚合列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:aggregation:export')")
    @Log(title = "服务聚合", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(InAggregation inAggregation)
    {
        List<InAggregation> list = inAggregationService.selectInAggregationList(inAggregation);
        ExcelUtil<InAggregation> util = new ExcelUtil<InAggregation>(InAggregation.class);
        return util.exportExcel(list, "aggregation");
    }

    /**
     * 获取服务聚合详细信息
     */
    @PreAuthorize("@ss.hasPermi('integrator:aggregation:query')")
    @GetMapping(value = { "/", "/{aggrId}" })
    public AjaxResult getInfo(@PathVariable(value = "aggrId", required = false) String aggrId)
    {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("forwardInfos", inForwardInfoService.selectInForwardInfoAll());
        if (StringUtils.isNotNull(aggrId))
        {
            ajax.put(AjaxResult.DATA_TAG, inAggregationService.selectInAggregationById(aggrId));
            ajax.put("forwardIds", inAggregationForwardService.selectInForwardIdListByArgId(aggrId));
        }
        return ajax;
    }

    /**
     * 新增服务聚合
     */
    @PreAuthorize("@ss.hasPermi('integrator:aggregation:add')")
    @Log(title = "服务聚合", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InAggregation inAggregation)
    {
        return toAjax(inAggregationService.insertInAggregation(inAggregation));
    }

    /**
     * 修改服务聚合
     */
    @PreAuthorize("@ss.hasPermi('integrator:aggregation:edit')")
    @Log(title = "服务聚合", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InAggregation inAggregation)
    {
        return toAjax(inAggregationService.updateInAggregation(inAggregation));
    }

    /**
     * 删除服务聚合
     */
    @PreAuthorize("@ss.hasPermi('integrator:aggregation:remove')")
    @Log(title = "服务聚合", businessType = BusinessType.DELETE)
	@DeleteMapping("/{aggrIds}")
    public AjaxResult remove(@PathVariable String[] aggrIds)
    {
        return toAjax(inAggregationService.deleteInAggregationByIds(aggrIds));
    }
}
