package com.ruoyi.integrator.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.integrator.domain.vo.InAppAccessAggrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.integrator.mapper.InAppAccessAggrMapper;
import com.ruoyi.integrator.domain.InAppAccessAggr;
import com.ruoyi.integrator.service.IInAppAccessAggrService;

/**
 * 应用聚合权限Service业务层处理
 * 
 * @author zhangxiulin
 * @date 2021-02-07
 */
@Service
public class InAppAccessAggrServiceImpl implements IInAppAccessAggrService 
{
    @Autowired
    private InAppAccessAggrMapper inAppAccessAggrMapper;

    /**
     * 查询应用聚合权限
     * 
     * @param appAggrId 应用聚合权限ID
     * @return 应用聚合权限
     */
    @Override
    public InAppAccessAggr selectInAppAccessAggrById(String appAggrId)
    {
        return inAppAccessAggrMapper.selectInAppAccessAggrById(appAggrId);
    }

    /**
     * 查询应用聚合权限列表
     * 
     * @param inAppAccessAggr 应用聚合权限
     * @return 应用聚合权限
     */
    @Override
    public List<InAppAccessAggr> selectInAppAccessAggrList(InAppAccessAggr inAppAccessAggr)
    {
        return inAppAccessAggrMapper.selectInAppAccessAggrList(inAppAccessAggr);
    }

    /**
     * 新增应用聚合权限
     * 
     * @param inAppAccessAggr 应用聚合权限
     * @return 结果
     */
    @Override
    public int insertInAppAccessAggr(InAppAccessAggr inAppAccessAggr)
    {
        inAppAccessAggr.setAppAggrId(IdUtils.fastSimpleUUID());
        inAppAccessAggr.setCreateBy(SecurityUtils.getUsername());
        inAppAccessAggr.setCreateTime(DateUtils.getNowDate());
        return inAppAccessAggrMapper.insertInAppAccessAggr(inAppAccessAggr);
    }

    /**
     * 修改应用聚合权限
     * 
     * @param inAppAccessAggr 应用聚合权限
     * @return 结果
     */
    @Override
    public int updateInAppAccessAggr(InAppAccessAggr inAppAccessAggr)
    {
        inAppAccessAggr.setUpdateBy(SecurityUtils.getUsername());
        inAppAccessAggr.setUpdateTime(DateUtils.getNowDate());
        return inAppAccessAggrMapper.updateInAppAccessAggr(inAppAccessAggr);
    }

    /**
     * 批量删除应用聚合权限
     * 
     * @param appAggrIds 需要删除的应用聚合权限ID
     * @return 结果
     */
    @Override
    public int deleteInAppAccessAggrByIds(String[] appAggrIds)
    {
        return inAppAccessAggrMapper.deleteInAppAccessAggrByIds(appAggrIds);
    }

    /**
     * 删除应用聚合权限信息
     * 
     * @param appAggrId 应用聚合权限ID
     * @return 结果
     */
    @Override
    public int deleteInAppAccessAggrById(String appAggrId)
    {
        return inAppAccessAggrMapper.deleteInAppAccessAggrById(appAggrId);
    }

    @Override
    public List<InAppAccessAggrVo> selectInAppAccessAggrVoList(InAppAccessAggr inAppAccessAggr) {
        return inAppAccessAggrMapper.selectInAppAccessAggrVoList(inAppAccessAggr);
    }
}
