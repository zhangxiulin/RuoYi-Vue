package com.ruoyi.demo.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.TccTryAjaxResult;
import com.ruoyi.common.enums.TccStage;
import com.ruoyi.common.utils.CalendarUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RandomUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.demo.config.TccConfig;
import com.ruoyi.demo.domain.DemoAccountWaterTcc;
import com.ruoyi.demo.domain.vo.DemoAccountWaterTccTryVo;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccCancelVo;
import com.ruoyi.demo.domain.vo.DemoTransferMoneyTccConfirmVo;
import com.ruoyi.demo.enums.JieDai;
import com.ruoyi.demo.mapper.DemoAccountWaterTccMapper;
import com.ruoyi.demo.service.IDemoAccountWaterTccService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TCC动账流水Service业务层处理
 * 
 * @author zhangxiulin
 * @date 2021-01-20
 */
@Service
public class DemoAccountWaterTccServiceImpl implements IDemoAccountWaterTccService 
{

    private static final Logger log = LoggerFactory.getLogger(DemoAccountWaterTccServiceImpl.class);

    @Autowired
    private TccConfig tccConfig;

    @Autowired
    private DemoAccountWaterTccMapper demoAccountWaterTccMapper;

    /**
     * 查询TCC动账流水
     * 
     * @param id TCC动账流水ID
     * @return TCC动账流水
     */
    @Override
    public DemoAccountWaterTcc selectDemoAccountWaterTccById(Long id)
    {
        return demoAccountWaterTccMapper.selectDemoAccountWaterTccById(id);
    }

    /**
     * 查询TCC动账流水列表
     * 
     * @param demoAccountWaterTcc TCC动账流水
     * @return TCC动账流水
     */
    @Override
    public List<DemoAccountWaterTcc> selectDemoAccountWaterTccList(DemoAccountWaterTcc demoAccountWaterTcc)
    {
        return demoAccountWaterTccMapper.selectDemoAccountWaterTccList(demoAccountWaterTcc);
    }

    /**
     * 新增TCC动账流水
     * 
     * @param demoAccountWaterTcc TCC动账流水
     * @return 结果
     */
    @Override
    public int insertDemoAccountWaterTcc(DemoAccountWaterTcc demoAccountWaterTcc)
    {
        demoAccountWaterTcc.setCreateTime(DateUtils.getNowDate());
        return demoAccountWaterTccMapper.insertDemoAccountWaterTcc(demoAccountWaterTcc);
    }

    /**
     * 修改TCC动账流水
     * 
     * @param demoAccountWaterTcc TCC动账流水
     * @return 结果
     */
    @Override
    public int updateDemoAccountWaterTcc(DemoAccountWaterTcc demoAccountWaterTcc)
    {
        demoAccountWaterTcc.setUpdateTime(DateUtils.getNowDate());
        return demoAccountWaterTccMapper.updateDemoAccountWaterTcc(demoAccountWaterTcc);
    }

    /**
     * 批量删除TCC动账流水
     * 
     * @param ids 需要删除的TCC动账流水ID
     * @return 结果
     */
    @Override
    public int deleteDemoAccountWaterTccByIds(Long[] ids)
    {
        return demoAccountWaterTccMapper.deleteDemoAccountWaterTccByIds(ids);
    }

    /**
     * 删除TCC动账流水信息
     * 
     * @param id TCC动账流水ID
     * @return 结果
     */
    @Override
    public int deleteDemoAccountWaterTccById(Long id)
    {
        return demoAccountWaterTccMapper.deleteDemoAccountWaterTccById(id);
    }

    @Override
    public AjaxResult tryReserveMoney(DemoAccountWaterTccTryVo tryVo) {
        AjaxResult ajaxResult = null;
        if (StringUtils.isNotEmpty(tryVo.getJdFlag())){
            JieDai jieDaiEnum = JieDai.valueOf(tryVo.getJdFlag());
            DemoAccountWaterTcc demoAccountWaterTcc = new DemoAccountWaterTcc();
            String serialNumber = "";
            if (JieDai.J == jieDaiEnum) {
                serialNumber = "J" + DateUtils.dateTimeNow() + RandomUtils.genRandomCharAndNumr(4);
                demoAccountWaterTcc.setSerialNumber(serialNumber);
                demoAccountWaterTcc.setjMoney(tryVo.getMoney());
            } else if (JieDai.D == jieDaiEnum) {
                serialNumber = "D" + DateUtils.dateTimeNow() + RandomUtils.genRandomCharAndNumr(4);
                demoAccountWaterTcc.setSerialNumber(serialNumber);
                demoAccountWaterTcc.setdMoney(tryVo.getMoney());
            } else {
                ajaxResult = AjaxResult.error("借贷标志非法");
                return ajaxResult;
            }
            demoAccountWaterTcc.setJdFlag(tryVo.getJdFlag());
            demoAccountWaterTcc.setUserCode(tryVo.getAccount());
            demoAccountWaterTcc.setTccStage(TccStage.TRIED.name());
            demoAccountWaterTcc.setStatus(Constants.STATUS_NORMAL);
            demoAccountWaterTcc.setCreateTime(DateUtils.getNowDate());
            // 过期时间
            String expireTimeStr = CalendarUtils.getExpireTimeStr(tccConfig.getTccExpiresDelayMs());
            demoAccountWaterTcc.setExpires(expireTimeStr);
            int iCount = this.demoAccountWaterTccMapper.insertDemoAccountWaterTcc(demoAccountWaterTcc);
            if (iCount > 0){
                String msg = "尝试成功";
                // 确认url 过期时间
                TccTryAjaxResult tccTryAjaxResult = TccTryAjaxResult.confirm(tccConfig.getTccConfirmUrl(), tccConfig.getTccCancelUrl(),
                        expireTimeStr, serialNumber);
                log.info(msg + "流水号[" + serialNumber + "]", tccTryAjaxResult);
                ajaxResult = new AjaxResult(HttpStatus.SUCCESS, msg, tccTryAjaxResult);
            } else {
                String msg = "尝试失败";
                log.error(msg + "，流水号[" + serialNumber + "]");
                ajaxResult = new AjaxResult(HttpStatus.ERROR, msg );
            }
        } else {
            ajaxResult = AjaxResult.error("借贷标志为空");
            return ajaxResult;
        }

        return ajaxResult;
    }

    @Override
    public AjaxResult confirmMoney(DemoTransferMoneyTccConfirmVo confirmVo) {
        AjaxResult ajaxResult = null;
        String serialNumber = confirmVo.getSerialNumber();
        if (StringUtils.isEmpty(serialNumber)){
            String msg = "确认失败，流水号为空";
            log.error(msg);
            ajaxResult = new AjaxResult(HttpStatus.ERROR, msg );
            return ajaxResult;
        }
        DemoAccountWaterTcc demoAccountWaterTcc = this.demoAccountWaterTccMapper.selectDemoAccountWaterTccBySerialNumber(serialNumber);
        // 检验状态
        if (demoAccountWaterTcc == null) {
            String msg = "确认失败，流水号未匹配到记录";
            log.error(msg + "，流水号[" + serialNumber + "]");
            ajaxResult = new AjaxResult(HttpStatus.ERROR, msg );
            return ajaxResult;
        }
        String tccStage = demoAccountWaterTcc.getTccStage();
        if (!TccStage.TRIED.name().equals(tccStage)) {
            String msg = "确认失败，转账记录状态非法";
            log.error(msg + "，流水号[" + serialNumber + "]");
            ajaxResult = new AjaxResult(HttpStatus.ERROR, msg );
            return ajaxResult;
        }
        // 修改状态
        demoAccountWaterTcc.setUpdateTime(DateUtils.getNowDate());
        demoAccountWaterTcc.setTccStage(TccStage.CONFIRMED.name());
        int uCount = this.demoAccountWaterTccMapper.updateDemoAccountWaterTcc(demoAccountWaterTcc);
        if (uCount > 0){
            String msg = "确认成功";
            log.error(msg + "，流水号[" + serialNumber + "]");
            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, msg );
        } else {
            String msg = "确认失败";
            log.error(msg + "，流水号[" + serialNumber + "]");
            ajaxResult = new AjaxResult(HttpStatus.ERROR, msg );
        }
        return ajaxResult;
    }

    @Override
    public AjaxResult cancelMoney(DemoTransferMoneyTccCancelVo cancelVo) {
        AjaxResult ajaxResult = null;
        String serialNumber = cancelVo.getSerialNumber();
        if (StringUtils.isEmpty(serialNumber)){
            String msg = "取消失败，流水号为空";
            log.error(msg);
            ajaxResult = new AjaxResult(HttpStatus.ERROR, msg );
            return ajaxResult;
        }
        DemoAccountWaterTcc demoAccountWaterTcc = this.demoAccountWaterTccMapper.selectDemoAccountWaterTccBySerialNumber(serialNumber);
        // 检验状态
        if (demoAccountWaterTcc == null) {
            String msg = "取消失败，流水号未匹配到记录";
            log.error(msg + "，流水号[" + serialNumber + "]");
            ajaxResult = new AjaxResult(HttpStatus.ERROR, msg );
            return ajaxResult;
        }
        String tccStage = demoAccountWaterTcc.getTccStage();
        if (!TccStage.TRIED.name().equals(tccStage)) {
            String msg = "取消失败，转账记录状态非法";
            log.error(msg + "，流水号[" + serialNumber + "]");
            ajaxResult = new AjaxResult(HttpStatus.ERROR, msg );
            return ajaxResult;
        }
        // 修改状态
        demoAccountWaterTcc.setUpdateTime(DateUtils.getNowDate());
        demoAccountWaterTcc.setTccStage(TccStage.CONFIRMED.name());
        int uCount = this.demoAccountWaterTccMapper.updateDemoAccountWaterTcc(demoAccountWaterTcc);
        if (uCount > 0){
            String msg = "取消成功";
            log.error(msg + "，流水号[" + serialNumber + "]");
            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, msg );
        } else {
            String msg = "取消失败";
            log.error(msg + "，流水号[" + serialNumber + "]");
            ajaxResult = new AjaxResult(HttpStatus.ERROR, msg );
        }
        return ajaxResult;
    }


}
