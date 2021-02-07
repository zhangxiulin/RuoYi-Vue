package com.ruoyi.integrator.mapper;

import java.util.List;
import com.ruoyi.integrator.domain.InAppAccessForward;

/**
 * 应用转发权限MapperAuto接口
 *
 * 由代码生成器产生，为适应表结构变化，尽量不要修改
 *
 * @author zhangxiulin
 * @date 2021-02-04
 */
public interface InAppAccessForwardMapperAuto
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
     * 删除应用转发权限
     * 
     * @param appFwdId 应用转发权限ID
     * @return 结果
     */
    public int deleteInAppAccessForwardById(String appFwdId);

    /**
     * 批量删除应用转发权限
     * 
     * @param appFwdIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteInAppAccessForwardByIds(String[] appFwdIds);
}
