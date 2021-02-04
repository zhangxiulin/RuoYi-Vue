package com.ruoyi.integrator.mapper;

import java.util.List;
import com.ruoyi.integrator.domain.InAppAccess;

/**
 * 接入应用Mapper接口
 *
 * @author zhangxiulin
 * @date 2021-02-01
 */
public interface InAppAccessMapper extends InAppAccessMapperAuto
{

    InAppAccess selectInAppAccessByAppKey(String appKey);

}
