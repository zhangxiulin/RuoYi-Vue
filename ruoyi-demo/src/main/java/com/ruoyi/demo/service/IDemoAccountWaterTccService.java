package com.ruoyi.demo.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.demo.domain.DemoAccountWaterTcc;
import com.ruoyi.demo.domain.vo.DemoAccountWaterTccTryVo;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccCancelVo;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccConfirmVo;

/**
 * TCC动账流水Service接口
 * 
 * @author zhangxiulin
 * @date 2021-01-20
 */
public interface IDemoAccountWaterTccService 
{
    /**
     * 查询TCC动账流水
     * 
     * @param id TCC动账流水ID
     * @return TCC动账流水
     */
    public DemoAccountWaterTcc selectDemoAccountWaterTccById(Long id);

    /**
     * 查询TCC动账流水列表
     * 
     * @param demoAccountWaterTcc TCC动账流水
     * @return TCC动账流水集合
     */
    public List<DemoAccountWaterTcc> selectDemoAccountWaterTccList(DemoAccountWaterTcc demoAccountWaterTcc);

    /**
     * 新增TCC动账流水
     * 
     * @param demoAccountWaterTcc TCC动账流水
     * @return 结果
     */
    public int insertDemoAccountWaterTcc(DemoAccountWaterTcc demoAccountWaterTcc);

    /**
     * 修改TCC动账流水
     * 
     * @param demoAccountWaterTcc TCC动账流水
     * @return 结果
     */
    public int updateDemoAccountWaterTcc(DemoAccountWaterTcc demoAccountWaterTcc);

    /**
     * 批量删除TCC动账流水
     * 
     * @param ids 需要删除的TCC动账流水ID
     * @return 结果
     */
    public int deleteDemoAccountWaterTccByIds(Long[] ids);

    /**
     * 删除TCC动账流水信息
     * 
     * @param id TCC动账流水ID
     * @return 结果
     */
    public int deleteDemoAccountWaterTccById(Long id);

    AjaxResult tryReserveMoney(DemoAccountWaterTccTryVo tryVo);

    AjaxResult confirmMoney(DemoTransferMoneyTccConfirmVo confirmVo);

    AjaxResult cancelMoney(DemoTransferMoneyTccCancelVo cancelVo);

}
