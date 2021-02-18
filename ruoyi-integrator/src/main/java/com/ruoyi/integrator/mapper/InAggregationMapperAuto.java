package com.ruoyi.integrator.mapper;

import java.util.List;
import com.ruoyi.integrator.domain.InAggregation;

/**
 * 服务聚合MapperAuto接口
 *
 * 由代码生成器产生，为适应表结构变化，请勿修改
 *
 * @author zhangxiulin
 * @date 2020-12-21
 */
public interface InAggregationMapperAuto
{
    /**
     * 查询服务聚合
     * 
     * @param aggrId 服务聚合ID
     * @return 服务聚合
     */
    public InAggregation selectInAggregationById(String aggrId);

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
     * 删除服务聚合
     * 
     * @param aggrId 服务聚合ID
     * @return 结果
     */
    public int deleteInAggregationById(String aggrId);

    /**
     * 批量删除服务聚合
     * 
     * @param aggrIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteInAggregationByIds(String[] aggrIds);

    InAggregation selectInAggregationByCode(String aggrCode);
}
