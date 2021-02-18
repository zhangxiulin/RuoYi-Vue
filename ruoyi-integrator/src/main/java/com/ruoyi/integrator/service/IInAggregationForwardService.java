package com.ruoyi.integrator.service;

import java.util.List;
import com.ruoyi.integrator.domain.InAggregationForward;

/**
 * 服务聚合关联Service接口
 * 
 * @author zhangxiulin
 * @date 2020-12-23
 */
public interface IInAggregationForwardService 
{
    /**
     * 查询服务聚合关联
     * 
     * @param aggrFwdId 服务聚合关联ID
     * @return 服务聚合关联
     */
    public InAggregationForward selectInAggregationForwardById(String aggrFwdId);

    /**
     * 查询服务聚合关联列表
     * 
     * @param inAggregationForward 服务聚合关联
     * @return 服务聚合关联集合
     */
    public List<InAggregationForward> selectInAggregationForwardList(InAggregationForward inAggregationForward);

    /**
     * 新增服务聚合关联
     * 
     * @param inAggregationForward 服务聚合关联
     * @return 结果
     */
    public int insertInAggregationForward(InAggregationForward inAggregationForward);

    /**
     * 修改服务聚合关联
     * 
     * @param inAggregationForward 服务聚合关联
     * @return 结果
     */
    public int updateInAggregationForward(InAggregationForward inAggregationForward);

    /**
     * 批量删除服务聚合关联
     * 
     * @param aggrFwdIds 需要删除的服务聚合关联ID
     * @return 结果
     */
    public int deleteInAggregationForwardByIds(String[] aggrFwdIds);

    /**
     * 删除服务聚合关联信息
     * 
     * @param aggrFwdId 服务聚合关联ID
     * @return 结果
     */
    public int deleteInAggregationForwardById(String aggrFwdId);

    List<String> selectInForwardIdListByArgId(String aggrId);
}
