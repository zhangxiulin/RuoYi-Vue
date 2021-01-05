package com.ruoyi.integrator.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.integrator.service.IInDataSourceTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/15 12:51
 */
@RestController
@RequestMapping("/integrator/datasourcetest")
public class InDataSourceTestController  extends BaseController {

    @Autowired
    private IInDataSourceTestService inDataSourceTestService;

    /**
     * 获取数据源详细信息
     */
    //@PreAuthorize("@ss.hasPermi('integrator:datasource:query')")
    @GetMapping(value = "/{datasourceId}")
    public AjaxResult getInfo(@PathVariable("datasourceId") String datasourceId)
    {
        return AjaxResult.success(inDataSourceTestService.selectInDatasourceById(datasourceId));
    }

    @GetMapping(value = "/conn/{datasourceId}")
    public AjaxResult getInfoConn(@PathVariable("datasourceId") String datasourceId)
    {
        return AjaxResult.success(inDataSourceTestService.selectInDatasourceByIdConn(datasourceId));
    }

}
