package com.ruoyi.integrator.service;

import java.util.List;
import com.ruoyi.integrator.domain.InAppAccessAggr;
import com.ruoyi.integrator.domain.vo.InAppAccessAggrVo;

/**
 * 应用聚合权限Service接口
 * 
 * @author zhangxiulin
 * @date 2021-02-07
 */
public interface IInAppAccessAggrService 
{
    /**
     * 查询应用聚合权限
     * 
     * @param appAggrId 应用聚合权限ID
     * @return 应用聚合权限
     */
    public InAppAccessAggr selectInAppAccessAggrById(String appAggrId);

    /**
     * 查询应用聚合权限列表
     * 
     * @param inAppAccessAggr 应用聚合权限
     * @return 应用聚合权限集合
     */
    public List<InAppAccessAggr> selectInAppAccessAggrList(InAppAccessAggr inAppAccessAggr);

    /**
     * 新增应用聚合权限
     * 
     * @param inAppAccessAggr 应用聚合权限
     * @return 结果
     */
    public int insertInAppAccessAggr(InAppAccessAggr inAppAccessAggr);

    /**
     * 修改应用聚合权限
     * 
     * @param inAppAccessAggr 应用聚合权限
     * @return 结果
     */
    public int updateInAppAccessAggr(InAppAccessAggr inAppAccessAggr);

    /**
     * 批量删除应用聚合权限
     * 
     * @param appAggrIds 需要删除的应用聚合权限ID
     * @return 结果
     */
    public int deleteInAppAccessAggrByIds(String[] appAggrIds);

    /**
     * 删除应用聚合权限信息
     * 
     * @param appAggrId 应用聚合权限ID
     * @return 结果
     */
    public int deleteInAppAccessAggrById(String appAggrId);

    List<InAppAccessAggrVo> selectInAppAccessAggrVoList(InAppAccessAggr inAppAccessAggr);
}
