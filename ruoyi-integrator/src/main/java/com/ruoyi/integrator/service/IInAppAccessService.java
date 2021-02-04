package com.ruoyi.integrator.service;

import java.util.List;
import com.ruoyi.integrator.domain.InAppAccess;

/**
 * 接入应用Service接口
 * 
 * @author zhangxiulin
 * @date 2021-02-01
 */
public interface IInAppAccessService 
{
    /**
     * 查询接入应用
     * 
     * @param appId 接入应用ID
     * @return 接入应用
     */
    public InAppAccess selectInAppAccessById(String appId);

    /**
     * 查询接入应用列表
     * 
     * @param inAppAccess 接入应用
     * @return 接入应用集合
     */
    public List<InAppAccess> selectInAppAccessList(InAppAccess inAppAccess);

    /**
     * 新增接入应用
     * 
     * @param inAppAccess 接入应用
     * @return 结果
     */
    public int insertInAppAccess(InAppAccess inAppAccess);

    /**
     * 修改接入应用
     * 
     * @param inAppAccess 接入应用
     * @return 结果
     */
    public int updateInAppAccess(InAppAccess inAppAccess);

    /**
     * 批量删除接入应用
     * 
     * @param appIds 需要删除的接入应用ID
     * @return 结果
     */
    public int deleteInAppAccessByIds(String[] appIds);

    /**
     * 删除接入应用信息
     * 
     * @param appId 接入应用ID
     * @return 结果
     */
    public int deleteInAppAccessById(String appId);

    /**
     *@Description: 根据AppKey查询AppAccess实体
     *@params:
     *@return:
     */
    InAppAccess selectInAppAccessByAppKey(String appKey);
}
