package com.ruoyi.integrator.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.jdbc.MysqlXAConnection;
import com.mysql.cj.jdbc.MysqlXid;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.database.DatabaseAccessLayer;
import com.ruoyi.common.core.database.ops.Table;
import com.ruoyi.common.core.database.ops.TableService;
import com.ruoyi.common.core.database.ops.TableUtils;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ExecutionOrder;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PatternUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.VelocityUtils2;
import com.ruoyi.integrator.domain.InAggregation;
import com.ruoyi.integrator.domain.InForwardInfo;
import com.ruoyi.integrator.enums.InForwardProtocol;
import com.ruoyi.integrator.enums.InForwardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @description: 三阶段提交（Three Phase Commit）分布式事务聚合服务转发
 *
 * @author: zhangxiulin
 * @time: 2020/12/27 22:59
 */
public class Dtx3pcAggregateThread implements Callable<AjaxResult> {

    private static final Logger logger = LoggerFactory.getLogger(Dtx3pcAggregateThread.class);

    private InAggregation inAggregation;

    private List sendVarList;

    private List sendDataList;

    public Dtx3pcAggregateThread(InAggregation inAggregation, List sendVarList, List sendDataList){
        this.inAggregation = inAggregation;
        this.sendVarList = sendVarList;
        this.sendDataList = sendDataList;
    }

    @Override
    public AjaxResult call() throws Exception {
        logger.info("开始处理聚合服务["+inAggregation.getAgrCode()+"] 分布式事务[3PC]...");
        AjaxResult ajaxResult = null;

        logger.info("聚合服务["+inAggregation.getAgrCode()+"] 分布式事务[3PC] 处理结束.");
        return ajaxResult;
    }
}
