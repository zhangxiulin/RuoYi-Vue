package com.ruoyi.integrator.service;

import java.util.List;
import com.ruoyi.integrator.domain.InAggregation;

/**
 * 服务聚合Service接口
 * 
 * @author zhangxiulin
 * @date 2020-12-21
 */
public interface IInAggregationService 
{
    /**
     * 查询服务聚合
     * 
     * @param agrId 服务聚合ID
     * @return 服务聚合
     */
    public InAggregation selectInAggregationById(String agrId);

    /**
     * 查询服务聚合列表
     * 
     * @param inAggregation 服务聚合
     * @return 服务聚合集合
     */
    public List<InAggregation> selectInAggregationList(InAggregation inAggregation);

    /**
     * 新增服务聚合
     * 
     * @param inAggregation 服务聚合
     * @return 结果
     */
    public int insertInAggregation(InAggregation inAggregation);

    /**
     * 修改服务聚合
     * 
     * @param inAggregation 服务聚合
     * @return 结果
     */
    public int updateInAggregation(InAggregation inAggregation);

    /**
     * 批量删除服务聚合
     * 
     * @param agrIds 需要删除的服务聚合ID
     * @return 结果
     */
    public int deleteInAggregationByIds(String[] agrIds);

    /**
     * 删除服务聚合信息
     * 
     * @param agrId 服务聚合ID
     * @return 结果
     */
    public int deleteInAggregationById(String agrId);

    InAggregation selectInAggregationByCode(String agrCode);
}
