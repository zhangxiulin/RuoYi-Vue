package com.ruoyi.integrator.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.integrator.domain.vo.InAppAccessForwardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.integrator.mapper.InAppAccessForwardMapper;
import com.ruoyi.integrator.domain.InAppAccessForward;
import com.ruoyi.integrator.service.IInAppAccessForwardService;

/**
 * 应用转发权限Service业务层处理
 * 
 * @author zhangxiulin
 * @date 2021-02-04
 */
@Service
public class InAppAccessForwardServiceImpl implements IInAppAccessForwardService 
{
    @Autowired
    private InAppAccessForwardMapper inAppAccessForwardMapper;

    /**
     * 查询应用转发权限
     * 
     * @param appFwdId 应用转发权限ID
     * @return 应用转发权限
     */
    @Override
    public InAppAccessForward selectInAppAccessForwardById(String appFwdId)
    {
        return inAppAccessForwardMapper.selectInAppAccessForwardById(appFwdId);
    }

    /**
     * 查询应用转发权限列表
     * 
     * @param inAppAccessForward 应用转发权限
     * @return 应用转发权限
     */
    @Override
    public List<InAppAccessForward> selectInAppAccessForwardList(InAppAccessForward inAppAccessForward)
    {
        return inAppAccessForwardMapper.selectInAppAccessForwardList(inAppAccessForward);
    }

    /**
     * 新增应用转发权限
     * 
     * @param inAppAccessForward 应用转发权限
     * @return 结果
     */
    @Override
    public int insertInAppAccessForward(InAppAccessForward inAppAccessForward)
    {
        inAppAccessForward.setAppFwdId(IdUtils.fastSimpleUUID());
        inAppAccessForward.setCreateBy(SecurityUtils.getUsername());
        inAppAccessForward.setCreateTime(DateUtils.getNowDate());
        return inAppAccessForwardMapper.insertInAppAccessForward(inAppAccessForward);
    }

    /**
     * 修改应用转发权限
     * 
     * @param inAppAccessForward 应用转发权限
     * @return 结果
     */
    @Override
    public int updateInAppAccessForward(InAppAccessForward inAppAccessForward)
    {
        inAppAccessForward.setCreateBy(SecurityUtils.getUsername());
        inAppAccessForward.setUpdateTime(DateUtils.getNowDate());
        return inAppAccessForwardMapper.updateInAppAccessForward(inAppAccessForward);
    }

    /**
     * 批量删除应用转发权限
     * 
     * @param appFwdIds 需要删除的应用转发权限ID
     * @return 结果
     */
    @Override
    public int deleteInAppAccessForwardByIds(String[] appFwdIds)
    {
        return inAppAccessForwardMapper.deleteInAppAccessForwardByIds(appFwdIds);
    }

    /**
     * 删除应用转发权限信息
     * 
     * @param appFwdId 应用转发权限ID
     * @return 结果
     */
    @Override
    public int deleteInAppAccessForwardById(String appFwdId)
    {
        return inAppAccessForwardMapper.deleteInAppAccessForwardById(appFwdId);
    }

    @Override
    public List<InAppAccessForwardVo> selectInAppAccessForwardVoList(InAppAccessForward inAppAccessForward) {
        return inAppAccessForwardMapper.selectInAppAccessForwardVoList(inAppAccessForward);
    }
}
