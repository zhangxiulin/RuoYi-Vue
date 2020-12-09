package com.ruoyi.integrator.mapper;

import java.util.List;
import com.ruoyi.integrator.domain.InForwardInfo;

/**
 * 转发配置Mapper接口
 *
 * @author zhangxiulin
 * @date 2020-12-02
 */
public interface InForwardInfoMapper extends InForwardInfoMapperAuto
{
    public InForwardInfo selectInForwardInfoByCode(String forwardCode);
}
