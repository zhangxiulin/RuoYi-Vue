package com.ruoyi.demo.service;

import java.util.List;
import com.ruoyi.demo.domain.DemoAccount;

/**
 * 账户Service接口
 * 
 * @author zhangxiulin
 * @date 2021-01-20
 */
public interface IDemoAccountService 
{
    /**
     * 查询账户
     * 
     * @param id 账户ID
     * @return 账户
     */
    public DemoAccount selectDemoAccountById(Long id);

    /**
     * 查询账户列表
     * 
     * @param demoAccount 账户
     * @return 账户集合
     */
    public List<DemoAccount> selectDemoAccountList(DemoAccount demoAccount);

    /**
     * 新增账户
     * 
     * @param demoAccount 账户
     * @return 结果
     */
    public int insertDemoAccount(DemoAccount demoAccount);

    /**
     * 修改账户
     * 
     * @param demoAccount 账户
     * @return 结果
     */
    public int updateDemoAccount(DemoAccount demoAccount);

    /**
     * 批量删除账户
     * 
     * @param ids 需要删除的账户ID
     * @return 结果
     */
    public int deleteDemoAccountByIds(Long[] ids);

    /**
     * 删除账户信息
     * 
     * @param id 账户ID
     * @return 结果
     */
    public int deleteDemoAccountById(Long id);
}
