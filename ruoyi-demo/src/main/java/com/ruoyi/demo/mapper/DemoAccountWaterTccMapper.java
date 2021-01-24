package com.ruoyi.demo.mapper;

import java.util.List;
import com.ruoyi.demo.domain.DemoAccountWaterTcc;

/**
 * TCC动账流水Mapper接口
 *
 * @author zhangxiulin
 * @date 2021-01-20
 */
public interface DemoAccountWaterTccMapper extends DemoAccountWaterTccMapperAuto
{
    DemoAccountWaterTcc selectDemoAccountWaterTccBySerialNumber(String serialNumber);
}
