package com.ruoyi.integrator.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.integrator.mapper.InAppAccessMapper;
import com.ruoyi.integrator.domain.InAppAccess;
import com.ruoyi.integrator.service.IInAppAccessService;

/**
 * 接入应用Service业务层处理
 * 
 * @author zhangxiulin
 * @date 2021-02-01
 */
@Service
public class InAppAccessServiceImpl implements IInAppAccessService 
{
    @Autowired
    private InAppAccessMapper inAppAccessMapper;

    /**
     * 查询接入应用
     * 
     * @param appId 接入应用ID
     * @return 接入应用
     */
    @Override
    public InAppAccess selectInAppAccessById(String appId)
    {
        return inAppAccessMapper.selectInAppAccessById(appId);
    }

    /**
     * 查询接入应用列表
     * 
     * @param inAppAccess 接入应用
     * @return 接入应用
     */
    @Override
    public List<InAppAccess> selectInAppAccessList(InAppAccess inAppAccess)
    {
        return inAppAccessMapper.selectInAppAccessList(inAppAccess);
    }

    /**
     * 新增接入应用
     * 
     * @param inAppAccess 接入应用
     * @return 结果
     */
    @Override
    public int insertInAppAccess(InAppAccess inAppAccess)
    {
        inAppAccess.setAppId(IdUtils.fastSimpleUUID());
        inAppAccess.setCreateBy(SecurityUtils.getUsername());
        inAppAccess.setCreateTime(DateUtils.getNowDate());
        return inAppAccessMapper.insertInAppAccess(inAppAccess);
    }

    /**
     * 修改接入应用
     * 
     * @param inAppAccess 接入应用
     * @return 结果
     */
    @Override
    public int updateInAppAccess(InAppAccess inAppAccess)
    {
        inAppAccess.setUpdateBy(SecurityUtils.getUsername());
        inAppAccess.setUpdateTime(DateUtils.getNowDate());
        return inAppAccessMapper.updateInAppAccess(inAppAccess);
    }

    /**
     * 批量删除接入应用
     * 
     * @param appIds 需要删除的接入应用ID
     * @return 结果
     */
    @Override
    public int deleteInAppAccessByIds(String[] appIds)
    {
        return inAppAccessMapper.deleteInAppAccessByIds(appIds);
    }

    /**
     * 删除接入应用信息
     * 
     * @param appId 接入应用ID
     * @return 结果
     */
    @Override
    public int deleteInAppAccessById(String appId)
    {
        return inAppAccessMapper.deleteInAppAccessById(appId);
    }

    @Override
    public InAppAccess selectInAppAccessByAppKey(String appKey) {
        return inAppAccessMapper.selectInAppAccessByAppKey(appKey);
    }
}
