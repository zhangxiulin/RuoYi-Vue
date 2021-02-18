package com.ruoyi.integrator.mapper;

import java.util.List;
import com.ruoyi.integrator.domain.InAggregationForward;

/**
 * 服务聚合关联Mapper接口
 *
 * @author zhangxiulin
 * @date 2020-12-23
 */
public interface InAggregationForwardMapper extends InAggregationForwardMapperAuto
{
    List<String> selectInForwardIdListByAggrId(String aggrId);

    int batchInAggregationForward(List<InAggregationForward> argFwdList);

    int deleteInAggregationForwardByAggrId(String aggrId);
}
