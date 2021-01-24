package com.ruoyi.demo.mapper;

import java.util.List;
import com.ruoyi.demo.domain.DemoAccountWaterTcc;

/**
 * TCC动账流水MapperAuto接口
 *
 * 由代码生成器产生，为适应表结构变化，尽量不要修改
 *
 * @author zhangxiulin
 * @date 2021-01-20
 */
public interface DemoAccountWaterTccMapperAuto
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
     * 删除TCC动账流水
     * 
     * @param id TCC动账流水ID
     * @return 结果
     */
    public int deleteDemoAccountWaterTccById(Long id);

    /**
     * 批量删除TCC动账流水
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDemoAccountWaterTccByIds(Long[] ids);
}
