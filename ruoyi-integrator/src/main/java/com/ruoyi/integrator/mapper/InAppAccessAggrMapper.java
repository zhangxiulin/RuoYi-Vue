package com.ruoyi.integrator.mapper;

import java.util.List;
import com.ruoyi.integrator.domain.InAppAccessAggr;
import com.ruoyi.integrator.domain.InAppAccessForward;
import com.ruoyi.integrator.domain.vo.InAppAccessAggrVo;
import com.ruoyi.integrator.domain.vo.InAppAccessForwardVo;
import org.apache.ibatis.annotations.Param;

/**
 * 应用聚合权限Mapper接口
 *
 * @author zhangxiulin
 * @date 2021-02-07
 */
public interface InAppAccessAggrMapper extends InAppAccessAggrMapperAuto
{

    List<InAppAccessAggrVo> selectInAppAccessAggrVoList(InAppAccessAggr inAppAccessAggr);

    Integer countInAppAccessAggr(@Param("appKey") String appKey, @Param("aggrCode") String aggrCode);

}
