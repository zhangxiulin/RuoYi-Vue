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
import com.ruoyi.integrator.domain.InDatasource;
import com.ruoyi.integrator.service.IInDatasourceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 数据源Controller
 * 
 * @author zhangxiulin
 * @date 2020-12-09
 */
@RestController
@RequestMapping("/integrator/datasource")
public class InDatasourceController extends BaseController
{
    @Autowired
    private IInDatasourceService inDatasourceService;

    /**
     * 查询数据源列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:datasource:list')")
    @GetMapping("/list")
    public TableDataInfo list(InDatasource inDatasource)
    {
        startPage();
        List<InDatasource> list = inDatasourceService.selectInDatasourceList(inDatasource);
        return getDataTable(list);
    }

    /**
     * 导出数据源列表
     */
    @PreAuthorize("@ss.hasPermi('integrator:datasource:export')")
    @Log(title = "数据源", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(InDatasource inDatasource)
    {
        List<InDatasource> list = inDatasourceService.selectInDatasourceList(inDatasource);
        ExcelUtil<InDatasource> util = new ExcelUtil<InDatasource>(InDatasource.class);
        return util.exportExcel(list, "datasource");
    }

    /**
     * 获取数据源详细信息
     */
    @PreAuthorize("@ss.hasPermi('integrator:datasource:query')")
    @GetMapping(value = "/{datasourceId}")
    public AjaxResult getInfo(@PathVariable("datasourceId") String datasourceId)
    {
        return AjaxResult.success(inDatasourceService.selectInDatasourceById(datasourceId));
    }

    /**
     * 新增数据源
     */
    @PreAuthorize("@ss.hasPermi('integrator:datasource:add')")
    @Log(title = "数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InDatasource inDatasource)
    {
        return toAjax(inDatasourceService.insertInDatasource(inDatasource));
    }

    /**
     * 修改数据源
     */
    @PreAuthorize("@ss.hasPermi('integrator:datasource:edit')")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InDatasource inDatasource)
    {
        return toAjax(inDatasourceService.updateInDatasource(inDatasource));
    }

    /**
     * 删除数据源
     */
    @PreAuthorize("@ss.hasPermi('integrator:datasource:remove')")
    @Log(title = "数据源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{datasourceIds}")
    public AjaxResult remove(@PathVariable String[] datasourceIds)
    {
        return toAjax(inDatasourceService.deleteInDatasourceByIds(datasourceIds));
    }

    /**
     * 同步数据源
     */
    @PreAuthorize("@ss.hasPermi('integrator:datasource:synchDs')")
    @GetMapping("/synchDs/{datasourceId}")
    public AjaxResult synchDs(@PathVariable String datasourceId)
    {
        if (inDatasourceService.synchDs(datasourceId)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }
}
