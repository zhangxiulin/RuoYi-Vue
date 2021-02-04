package com.ruoyi.integrator.service;

import com.ruoyi.common.core.domain.AjaxResult;

import java.util.List;

/**
 *  TCC 事务管理/协调器
 *
 * @author zhangxiulin
 * @date 2020-12-02
 */
public interface ITransactionCoordinator {

    /**
     * 协调器会对参与者逐个发起Confirm请求, 如果一切顺利那么将会返回如下结果 HTTP/1.1 204 No Content
     * 如果发起Confirm请求的时间太晚, 那么意味着所有被动方都已经进行了超时补偿  HTTP/1.1 404 Not Found
     * 最糟糕的情况就是有些参与者确认了, 但是有些就没有. 这种情况就应该要返回409, 需要人工干预，这种情况在Atomikos中定义为启发式异常 HTTP/1.1 409 Conflict
     */
    AjaxResult confirm(List<AjaxResult> fwdAjaxResultList) throws Exception;

    /**
     * 当预留资源没有被确认时最后都会被释放, 所以参与者返回其他错误也不会影响最终一致性  HTTP/1.1 204 No Content
     */
    AjaxResult cancel(List<AjaxResult> fwdAjaxResultList) throws Exception;

}
