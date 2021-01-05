package com.ruoyi.integrator.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.integrator.domain.InAggregationForward;
import com.ruoyi.integrator.mapper.InAggregationForwardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.integrator.mapper.InAggregationMapper;
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.service.IInAggregationService;

/**
 * 服务聚合Service业务层处理
 * 
 * @author zhangxiulin
 * @date 2020-12-21
 */
@Service
public class InAggregationServiceImpl implements IInAggregationService 
{
    @Autowired
    private InAggregationMapper inAggregationMapper;

    @Autowired
    private InAggregationForwardMapper inAggregationForwardMapper;

    /**
     * 查询服务聚合
     * 
     * @param agrId 服务聚合ID
     * @return 服务聚合
     */
    @Override
    public InAggregation selectInAggregationById(String agrId)
    {
        return inAggregationMapper.selectInAggregationById(agrId);
    }

    /**
     * 查询服务聚合列表
     * 
     * @param inAggregation 服务聚合
     * @return 服务聚合
     */
    @Override
    public List<InAggregation> selectInAggregationList(InAggregation inAggregation)
    {
        return inAggregationMapper.selectInAggregationList(inAggregation);
    }

    /**
     * 新增服务聚合
     * 
     * @param inAggregation 服务聚合
     * @return 结果
     */
    @Override
    public int insertInAggregation(InAggregation inAggregation)
    {
        inAggregation.setAgrId(IdUtils.fastSimpleUUID());
        inAggregation.setCreateBy(SecurityUtils.getUsername());
        inAggregation.setCreateTime(DateUtils.getNowDate());
        int rows = inAggregationMapper.insertInAggregation(inAggregation);
        // 插入服务接口关联关系
        insertInAggregationForward(inAggregation);
        return rows;
    }

    /**
     * 修改服务聚合
     * 
     * @param inAggregation 服务聚合
     * @return 结果
     */
    @Override
    public int updateInAggregation(InAggregation inAggregation)
    {
        inAggregation.setUpdateBy(SecurityUtils.getUsername());
        inAggregation.setUpdateTime(DateUtils.getNowDate());
        int row = inAggregationMapper.updateInAggregation(inAggregation);
        // 删除旧的服务接口关联关系
        inAggregationForwardMapper.deleteInAggregationForwardByAgrId(inAggregation.getAgrId());
        // 插入新的服务接口关联关系
        insertInAggregationForward(inAggregation);
        return row;
    }

    /**
     * 批量删除服务聚合
     * 
     * @param agrIds 需要删除的服务聚合ID
     * @return 结果
     */
    @Override
    public int deleteInAggregationByIds(String[] agrIds)
    {
        return inAggregationMapper.deleteInAggregationByIds(agrIds);
    }

    /**
     * 删除服务聚合信息
     * 
     * @param agrId 服务聚合ID
     * @return 结果
     */
    @Override
    public int deleteInAggregationById(String agrId)
    {
        return inAggregationMapper.deleteInAggregationById(agrId);
    }

    @Override
    public InAggregation selectInAggregationByCode(String agrCode) {
        return inAggregationMapper.selectInAggregationByCode(agrCode);
    }

    public void insertInAggregationForward(InAggregation inAggregation) {
        String[] forwardIds = inAggregation.getForwardIds();
        if (StringUtils.isNotEmpty(forwardIds)){
            List<InAggregationForward> inAggregationForwardList = new ArrayList<>();
            for (int i=0,len=forwardIds.length; i<len; i++){
                InAggregationForward inAggregationForward = new InAggregationForward();
                inAggregationForward.setAgrFwdId(IdUtils.fastSimpleUUID());
                inAggregationForward.setFwdId(forwardIds[i]);
                inAggregationForward.setAgrId(inAggregation.getAgrId());
                inAggregationForward.setOrderNum(i);
                inAggregationForwardList.add(inAggregationForward);
            }
            if (inAggregationForwardList.size() > 0){
                inAggregationForwardMapper.batchInAggregationForward(inAggregationForwardList);
            }
        }
    }
}
