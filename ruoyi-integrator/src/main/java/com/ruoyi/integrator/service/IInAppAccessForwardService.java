package com.ruoyi.integrator.service;

import java.util.List;
import com.ruoyi.integrator.domain.InAppAccessForward;
import com.ruoyi.integrator.domain.vo.InAppAccessForwardVo;

/**
 * 应用转发权限Service接口
 * 
 * @author zhangxiulin
 * @date 2021-02-04
 */
public interface IInAppAccessForwardService 
{
    /**
     * 查询应用转发权限
     * 
     * @param appFwdId 应用转发权限ID
     * @return 应用转发权限
     */
    public InAppAccessForward selectInAppAccessForwardById(String appFwdId);

    /**
     * 查询应用转发权限列表
     * 
     * @param inAppAccessForward 应用转发权限
     * @return 应用转发权限集合
     */
    public List<InAppAccessForward> selectInAppAccessForwardList(InAppAccessForward inAppAccessForward);

    /**
     * 新增应用转发权限
     * 
     * @param inAppAccessForward 应用转发权限
     * @return 结果
     */
    public int insertInAppAccessForward(InAppAccessForward inAppAccessForward);

    /**
     * 修改应用转发权限
     * 
     * @param inAppAccessForward 应用转发权限
     * @return 结果
     */
    public int updateInAppAccessForward(InAppAccessForward inAppAccessForward);

    /**
     * 批量删除应用转发权限
     * 
     * @param appFwdIds 需要删除的应用转发权限ID
     * @return 结果
     */
    public int deleteInAppAccessForwardByIds(String[] appFwdIds);

    /**
     * 删除应用转发权限信息
     * 
     * @param appFwdId 应用转发权限ID
     * @return 结果
     */
    public int deleteInAppAccessForwardById(String appFwdId);

    List<InAppAccessForwardVo> selectInAppAccessForwardVoList(InAppAccessForward inAppAccessForward);
}
