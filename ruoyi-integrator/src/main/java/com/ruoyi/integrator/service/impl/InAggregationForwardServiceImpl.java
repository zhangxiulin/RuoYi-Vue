package com.ruoyi.integrator.service.impl;

import java.util.List;

import com.ruoyi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.integrator.mapper.InAggregationForwardMapper;
import com.ruoyi.integrator.domain.InAggregationForward;
import com.ruoyi.integrator.service.IInAggregationForwardService;

/**
 * 服务聚合关联Service业务层处理
 * 
 * @author zhangxiulin
 * @date 2020-12-23
 */
@Service
public class InAggregationForwardServiceImpl implements IInAggregationForwardService 
{
    @Autowired
    private InAggregationForwardMapper inAggregationForwardMapper;

    /**
     * 查询服务聚合关联
     * 
     * @param agrFwdId 服务聚合关联ID
     * @return 服务聚合关联
     */
    @Override
    public InAggregationForward selectInAggregationForwardById(String agrFwdId)
    {
        return inAggregationForwardMapper.selectInAggregationForwardById(agrFwdId);
    }

    /**
     * 查询服务聚合关联列表
     * 
     * @param inAggregationForward 服务聚合关联
     * @return 服务聚合关联
     */
    @Override
    public List<InAggregationForward> selectInAggregationForwardList(InAggregationForward inAggregationForward)
    {
        return inAggregationForwardMapper.selectInAggregationForwardList(inAggregationForward);
    }

    /**
     * 新增服务聚合关联
     * 
     * @param inAggregationForward 服务聚合关联
     * @return 结果
     */
    @Override
    public int insertInAggregationForward(InAggregationForward inAggregationForward)
    {
        inAggregationForward.setAgrFwdId(IdUtils.fastSimpleUUID());
        return inAggregationForwardMapper.insertInAggregationForward(inAggregationForward);
    }

    /**
     * 修改服务聚合关联
     * 
     * @param inAggregationForward 服务聚合关联
     * @return 结果
     */
    @Override
    public int updateInAggregationForward(InAggregationForward inAggregationForward)
    {
        return inAggregationForwardMapper.updateInAggregationForward(inAggregationForward);
    }

    /**
     * 批量删除服务聚合关联
     * 
     * @param agrFwdIds 需要删除的服务聚合关联ID
     * @return 结果
     */
    @Override
    public int deleteInAggregationForwardByIds(String[] agrFwdIds)
    {
        return inAggregationForwardMapper.deleteInAggregationForwardByIds(agrFwdIds);
    }

    /**
     * 删除服务聚合关联信息
     * 
     * @param agrFwdId 服务聚合关联ID
     * @return 结果
     */
    @Override
    public int deleteInAggregationForwardById(String agrFwdId)
    {
        return inAggregationForwardMapper.deleteInAggregationForwardById(agrFwdId);
    }

    @Override
    public List<String> selectInForwardIdListByArgId(String agrId) {
        return inAggregationForwardMapper.selectInForwardIdListByAgrId(agrId);
    }
}
