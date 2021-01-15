package com.ruoyi.integrator.enums;

/**
 * @description: 分布式事务解决方案种类
 *
 * DTP模型定义了两阶段提交，XA规范在此基础上定义了RM-TM的交互接口，还对两阶段提交协议进行了优化
 * JTA可以看做XA规范的java版本，由于Tomcat不像EJB Server那样实现了JTA规范，因此并不能提供事务管理器的功能，需要借助Atomikos这种第三方的事务管理器类库
 *
 * @author: zhangxiulin
 * @time: 2020/12/24 12:55
 */
public enum DistributedTxSolution {

    TwoPhaseCommit("2PC", "XA协议两阶段提交"),
    ThreePhaseCommit("3PC", "XA协议三阶段提交"),
    Atomikos("ATOMIKOS", "Atomikos JTA/XA全局事务管理器"),
    BestEffortDelivery("BED", "柔性事务最大努力通知"),
    TryConfirmCancel("TCC", "柔性事务两阶段补偿型"),
    Kafka("KAFKA", "Kafka消息队列方案"),
    Saga("SAGA", "Saga");

    private final String code;
    private final String name;

    DistributedTxSolution(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static DistributedTxSolution getEnumByCode(String code){
        for (DistributedTxSolution solution : DistributedTxSolution.values()){
            if (solution.getCode().equals(code)){
                return solution;
            }
        }
        return null;
    }
}
