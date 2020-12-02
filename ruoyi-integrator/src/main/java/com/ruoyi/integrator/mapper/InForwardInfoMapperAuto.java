package com.ruoyi.integrator.mapper;

import java.util.List;
import com.ruoyi.integrator.domain.InForwardInfo;

/**
 * 转发配置MapperAuto接口
 * 
 * @author zhangxiulin
 * @date 2020-12-02
 */
public interface InForwardInfoMapperAuto
{
    /**
     * 查询转发配置
     * 
     * @param infoId 转发配置ID
     * @return 转发配置
     */
    public InForwardInfo selectInForwardInfoById(String infoId);

    /**
     * 查询转发配置列表
     * 
     * @param inForwardInfo 转发配置
     * @return 转发配置集合
     */
    public List<InForwardInfo> selectInForwardInfoList(InForwardInfo inForwardInfo);

    /**
     * 新增转发配置
     * 
     * @param inForwardInfo 转发配置
     * @return 结果
     */
    public int insertInForwardInfo(InForwardInfo inForwardInfo);

    /**
     * 修改转发配置
     * 
     * @param inForwardInfo 转发配置
     * @return 结果
     */
    public int updateInForwardInfo(InForwardInfo inForwardInfo);

    /**
     * 删除转发配置
     * 
     * @param infoId 转发配置ID
     * @return 结果
     */
    public int deleteInForwardInfoById(String infoId);

    /**
     * 批量删除转发配置
     * 
     * @param infoIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteInForwardInfoByIds(String[] infoIds);
}
