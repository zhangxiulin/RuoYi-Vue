package com.ruoyi.demo.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.demo.mapper.DemoAccountMapper;
import com.ruoyi.demo.domain.DemoAccount;
import com.ruoyi.demo.service.IDemoAccountService;

/**
 * 账户Service业务层处理
 * 
 * @author zhangxiulin
 * @date 2021-01-20
 */
@Service
public class DemoAccountServiceImpl implements IDemoAccountService 
{
    @Autowired
    private DemoAccountMapper demoAccountMapper;

    /**
     * 查询账户
     * 
     * @param id 账户ID
     * @return 账户
     */
    @Override
    public DemoAccount selectDemoAccountById(Long id)
    {
        return demoAccountMapper.selectDemoAccountById(id);
    }

    /**
     * 查询账户列表
     * 
     * @param demoAccount 账户
     * @return 账户
     */
    @Override
    public List<DemoAccount> selectDemoAccountList(DemoAccount demoAccount)
    {
        return demoAccountMapper.selectDemoAccountList(demoAccount);
    }

    /**
     * 新增账户
     * 
     * @param demoAccount 账户
     * @return 结果
     */
    @Override
    public int insertDemoAccount(DemoAccount demoAccount)
    {
        demoAccount.setCreateTime(DateUtils.getNowDate());
        return demoAccountMapper.insertDemoAccount(demoAccount);
    }

    /**
     * 修改账户
     * 
     * @param demoAccount 账户
     * @return 结果
     */
    @Override
    public int updateDemoAccount(DemoAccount demoAccount)
    {
        demoAccount.setUpdateTime(DateUtils.getNowDate());
        return demoAccountMapper.updateDemoAccount(demoAccount);
    }

    /**
     * 批量删除账户
     * 
     * @param ids 需要删除的账户ID
     * @return 结果
     */
    @Override
    public int deleteDemoAccountByIds(Long[] ids)
    {
        return demoAccountMapper.deleteDemoAccountByIds(ids);
    }

    /**
     * 删除账户信息
     * 
     * @param id 账户ID
     * @return 结果
     */
    @Override
    public int deleteDemoAccountById(Long id)
    {
        return demoAccountMapper.deleteDemoAccountById(id);
    }
}
