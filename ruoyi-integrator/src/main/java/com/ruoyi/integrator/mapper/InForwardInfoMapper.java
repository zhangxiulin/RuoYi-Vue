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
    InForwardInfo selectInForwardInfoByCode(String forwardCode);

    List<InForwardInfo> selectInForwardInfoAll();

    List<InForwardInfo> selectInForwardInfoListByAgrCode(String agrCode);

    List<InForwardInfo> selectInForwardInfoListByAgrId(String agrId);
}
