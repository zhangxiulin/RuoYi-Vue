package com.ruoyi.demo.mapper;

import java.util.List;
import com.ruoyi.demo.domain.DemoAccount;

/**
 * 账户MapperAuto接口
 *
 * 由代码生成器产生，为适应表结构变化，尽量不要修改
 *
 * @author zhangxiulin
 * @date 2021-01-20
 */
public interface DemoAccountMapperAuto
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
     * 删除账户
     * 
     * @param id 账户ID
     * @return 结果
     */
    public int deleteDemoAccountById(Long id);

    /**
     * 批量删除账户
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDemoAccountByIds(Long[] ids);
}
