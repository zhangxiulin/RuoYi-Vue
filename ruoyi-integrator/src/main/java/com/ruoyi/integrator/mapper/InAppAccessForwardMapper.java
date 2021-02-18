package com.ruoyi.integrator.mapper;

import java.util.List;
import com.ruoyi.integrator.domain.InAppAccessForward;
import com.ruoyi.integrator.domain.vo.InAppAccessForwardVo;
import org.apache.ibatis.annotations.Param;

/**
 * 应用转发权限Mapper接口
 *
 * @author zhangxiulin
 * @date 2021-02-04
 */
public interface InAppAccessForwardMapper extends InAppAccessForwardMapperAuto
{

    List<InAppAccessForwardVo> selectInAppAccessForwardVoList(InAppAccessForward inAppAccessForward);

    Integer countInAppAccessForward(@Param("appKey") String appKey, @Param("forwardCode") String forwardCode);

}
