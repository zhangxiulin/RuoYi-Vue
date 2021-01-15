package com.ruoyi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.database.DatabaseAccessLayer;
import com.ruoyi.common.enums.DataSourceVendor;
import com.ruoyi.common.utils.PropertiesUtils;
import com.ruoyi.framework.datasource.jta.xadatasource.GetXADataSourceInstContext;
import com.ruoyi.framework.datasource.runtime.get.GetDataSourceInstContext;
import com.ruoyi.integrator.domain.InDatasource;
import com.ruoyi.integrator.service.IInDatasourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * @description: SpringBoot项目启动后触发
 * @author: zhangxiulin
 * @time: 2020/12/10 12:47
 */
@Component
public class RuoYiApplicationRunnerImpl implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(RuoYiApplicationRunnerImpl.class);

    @Autowired
    private IInDatasourceService iInDatasourceService;

    @Autowired
    private GetDataSourceInstContext getDataSourceInstContext;

    @Autowired
    private GetXADataSourceInstContext getXADataSourceInstContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 运行时动态添加配置文件中未配置的数据源
        try {
            log.info("运行时动态添加配置文件中未配置的数据源...");
            loadDynamicDatasource();
            log.info("运行时动态添加配置文件中未配置的数据源结束.");
        }catch (Exception e){
            log.error("运行时动态添加配置文件中未配置的数据源异常", e);
        }

        if (RuoYiConfig.isAtomikosEnabled()){
            // 运行时动态添加配置文件中未配置的XA数据源
            try {
                log.info("运行时动态添加配置文件中未配置的Atomikos JTA/XA数据源...");
                loadDynamicAtomikosDatasource();
                log.info("运行时动态添加配置文件中未配置的Atomikos JTA/XA数据源结束.");
            }catch (Exception e){
                log.error("运行时动态添加配置文件中未配置的Atomikos JTA/XA数据源异常", e);
            }
        }

    }

    private void loadDynamicDatasource(){
        InDatasource inDatasource = new InDatasource();
        inDatasource.setStatus(Constants.STATUS_NORMAL);
        List<InDatasource> inDatasourceList = iInDatasourceService.selectInDatasourceList(inDatasource);
        if (inDatasourceList != null){
            inDatasourceList.forEach( d -> {
                String datasourceName = d.getDatasourceName();
                String datasourceOptions = d.getDatasourceOptions();
                String datasourceType = d.getDatasourceType();
                log.info("开始加载数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] ...");
                DataSourceVendor dataSourceVendorEnum = null;
                try {
                    dataSourceVendorEnum = DataSourceVendor.valueOf(datasourceType);
                } catch (Throwable throwable){
                    log.error("数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 异常！不支持的类型" , throwable);
                    return; // 进行下一个元素
                }
                JSONObject datasourceOptionsJson = null;
                try {
                    datasourceOptionsJson = JSON.parseObject(datasourceOptions);
                } catch (Throwable throwable){
                    log.error("数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 异常！配置项不是JSON格式", throwable);
                    return;
                }
                // 转JSON格式配置项为Properties
                Properties datasourceProperties = null;
                try {
                    if (datasourceOptionsJson != null){
                        datasourceProperties = PropertiesUtils.json2Properties(datasourceOptionsJson);
                    }
                } catch (Throwable throwable){
                    log.error("数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 异常！配置项格式不正确", throwable);
                    return;
                }
                // 加载数据源
                try {
                    DataSource dataSourceInst = getDataSourceInstContext.getDataSourceInst(dataSourceVendorEnum, datasourceProperties);
                    DatabaseAccessLayer.DATASOURCE.put(datasourceName, dataSourceInst);
                } catch (Throwable throwable){
                    log.error("数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 异常！加载数据源异常", throwable);
                    return;
                }
                log.info("加载数据源[" + datasourceName + "] 连接池种类[" + datasourceType + "] 结束.");
            });
        }
    }

    private void loadDynamicAtomikosDatasource(){
        InDatasource inDatasource = new InDatasource();
        inDatasource.setStatus(Constants.STATUS_NORMAL);
        List<InDatasource> inDatasourceList = iInDatasourceService.selectInDatasourceList(inDatasource);
        if (inDatasourceList != null){
            inDatasourceList.forEach( d -> {
                iInDatasourceService.synchAtomikos(d.getDatasourceId());
            });
        }
    }
}
