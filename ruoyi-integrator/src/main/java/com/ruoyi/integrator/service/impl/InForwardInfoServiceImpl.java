package com.ruoyi.integrator.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.integrator.mapper.InForwardInfoMapper;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.service.IInForwardInfoService;

/**
 * 转发配置Service业务层处理
 * 
 * @author zhangxiulin
 * @date 2020-12-02
 */
@Service
public class InForwardInfoServiceImpl implements IInForwardInfoService 
{
    @Autowired
    private InForwardInfoMapper inForwardInfoMapper;

    /**
     * 查询转发配置
     * 
     * @param infoId 转发配置ID
     * @return 转发配置
     */
    @Override
    public InForwardInfo selectInForwardInfoById(String infoId)
    {
        return inForwardInfoMapper.selectInForwardInfoById(infoId);
    }

    /**
     * 查询转发配置列表
     * 
     * @param inForwardInfo 转发配置
     * @return 转发配置
     */
    @Override
    public List<InForwardInfo> selectInForwardInfoList(InForwardInfo inForwardInfo)
    {
        return inForwardInfoMapper.selectInForwardInfoList(inForwardInfo);
    }

    /**
     * 新增转发配置
     * 
     * @param inForwardInfo 转发配置
     * @return 结果
     */
    @Override
    public int insertInForwardInfo(InForwardInfo inForwardInfo)
    {
        inForwardInfo.setCreateTime(DateUtils.getNowDate());
        return inForwardInfoMapper.insertInForwardInfo(inForwardInfo);
    }

    /**
     * 修改转发配置
     * 
     * @param inForwardInfo 转发配置
     * @return 结果
     */
    @Override
    public int updateInForwardInfo(InForwardInfo inForwardInfo)
    {
        inForwardInfo.setUpdateTime(DateUtils.getNowDate());
        return inForwardInfoMapper.updateInForwardInfo(inForwardInfo);
    }

    /**
     * 批量删除转发配置
     * 
     * @param infoIds 需要删除的转发配置ID
     * @return 结果
     */
    @Override
    public int deleteInForwardInfoByIds(String[] infoIds)
    {
        return inForwardInfoMapper.deleteInForwardInfoByIds(infoIds);
    }

    /**
     * 删除转发配置信息
     * 
     * @param infoId 转发配置ID
     * @return 结果
     */
    @Override
    public int deleteInForwardInfoById(String infoId)
    {
        return inForwardInfoMapper.deleteInForwardInfoById(infoId);
    }
}
