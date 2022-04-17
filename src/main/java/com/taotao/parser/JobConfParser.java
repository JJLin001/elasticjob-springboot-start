package com.taotao.parser;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.taotao.base.JobAttributeTag;
import com.taotao.nanotation.ElasticJobConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/16/18:42
 */
@Component
@Configuration
public class JobConfParser implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(JobConfParser.class);

    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    private String prefix = "elastic-job.";
    private Environment environment;


    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.environment = ctx.getEnvironment();
        Map<String, Object> beanMap = ctx.getBeansWithAnnotation(ElasticJobConf.class);
        Iterator var3 = beanMap.values().iterator();

        while (var3.hasNext()) {
            Object confBean = var3.next();
            Class<?> clz = confBean.getClass();
            ElasticJobConf conf = (ElasticJobConf) clz.getAnnotation(ElasticJobConf.class);
            String jobTypeName = conf.type().getName();
            String jobClass = clz.getName();
            String jobName = conf.name();
            String cron = this.getEnvironmentStringValue(jobName, JobAttributeTag.CRON, conf.cron());
            String shardingItemParameters = this.getEnvironmentStringValue(jobName, JobAttributeTag.SHARDING_ITEM_PARAMETERS, conf.shardingItemParameters());
            String description = this.getEnvironmentStringValue(jobName, JobAttributeTag.DESCRIPTION, conf.description());
            String jobParameter = this.getEnvironmentStringValue(jobName, JobAttributeTag.JOB_PARAMETER, conf.jobParameter());
            String jobExceptionHandler = this.getEnvironmentStringValue(jobName, JobAttributeTag.JOB_EXCEPTION_HANDLER, conf.jobExceptionHandler());
            String executorServiceHandler = this.getEnvironmentStringValue(jobName, JobAttributeTag.EXECUTOR_SERVICE_HANDLER, conf.executorServiceHandler());
            String jobShardingStrategyClass = this.getEnvironmentStringValue(jobName, JobAttributeTag.JOB_SHARDING_STRATEGY_CLASS, conf.jobShardingStrategyClass());
            String eventTraceRdbDataSource = this.getEnvironmentStringValue(jobName, JobAttributeTag.EVENT_TRACE_RDB_DATA_SOURCE, conf.eventTraceRdbDataSource());
            String scriptCommandLine = this.getEnvironmentStringValue(jobName, JobAttributeTag.SCRIPT_COMMAND_LINE, conf.scriptCommandLine());
            boolean failover = this.getEnvironmentBooleanValue(jobName, JobAttributeTag.FAILOVER, conf.failover());
            boolean misfire = this.getEnvironmentBooleanValue(jobName, JobAttributeTag.MISFIRE, conf.misfire());
            boolean overwrite = this.getEnvironmentBooleanValue(jobName, JobAttributeTag.OVERWRITE, conf.overwrite());
            boolean disabled = this.getEnvironmentBooleanValue(jobName, JobAttributeTag.DISABLED, conf.disabled());
            boolean monitorExecution = this.getEnvironmentBooleanValue(jobName, JobAttributeTag.MONITOR_EXECUTION, conf.monitorExecution());
            boolean streamingProcess = this.getEnvironmentBooleanValue(jobName, JobAttributeTag.STREAMING_PROCESS, conf.streamingProcess());
            int shardingTotalCount = this.getEnvironmentIntValue(jobName, JobAttributeTag.SHARDING_TOTAL_COUNT, conf.shardingTotalCount());
            int monitorPort = this.getEnvironmentIntValue(jobName, JobAttributeTag.MONITOR_PORT, conf.monitorPort());
            int maxTimeDiffSeconds = this.getEnvironmentIntValue(jobName, JobAttributeTag.MAX_TIME_DIFF_SECONDS, conf.maxTimeDiffSeconds());
            int reconcileIntervalMinutes = this.getEnvironmentIntValue(jobName, JobAttributeTag.RECONCILE_INTERVAL_MINUTES, conf.reconcileIntervalMinutes());
            JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).description(description).failover(failover).jobParameter(jobParameter).misfire(misfire).jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobExceptionHandler).jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), executorServiceHandler).build();
            LiteJobConfiguration jobConfig = null;
            JobTypeConfiguration typeConfig = null;
            if (jobTypeName.equals("SimpleJob")) {
                typeConfig = new SimpleJobConfiguration(coreConfig, jobClass);
            }

            if (jobTypeName.equals("DataflowJob")) {
                typeConfig = new DataflowJobConfiguration(coreConfig, jobClass, streamingProcess);
            }

            if (jobTypeName.equals("ScriptJob")) {
                typeConfig = new ScriptJobConfiguration(coreConfig, scriptCommandLine);
            }

            jobConfig = LiteJobConfiguration.newBuilder((JobTypeConfiguration) typeConfig).overwrite(overwrite).disabled(disabled).monitorPort(monitorPort).monitorExecution(monitorExecution).maxTimeDiffSeconds(maxTimeDiffSeconds).jobShardingStrategyClass(jobShardingStrategyClass).reconcileIntervalMinutes(reconcileIntervalMinutes).build();
            List<BeanDefinition> elasticJobListeners = this.getTargetElasticJobListeners(conf);
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
            factory.setScope("prototype");
            if ("ScriptJob".equals(jobTypeName)) {
                factory.addConstructorArgValue((Object) null);
            } else {
                factory.addConstructorArgValue(confBean);
            }

            factory.addConstructorArgValue(this.zookeeperRegistryCenter);
            factory.addConstructorArgValue(jobConfig);

            if (StringUtils.hasText(eventTraceRdbDataSource)) {
                BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
                rdbFactory.addConstructorArgReference(eventTraceRdbDataSource);
                factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
            }

            factory.addConstructorArgValue(elasticJobListeners);
            String registerBeanName = jobName + "SpringJobScheduler";
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
            defaultListableBeanFactory.registerBeanDefinition(registerBeanName, factory.getBeanDefinition());
            SpringJobScheduler springJobScheduler = (SpringJobScheduler) ctx.getBean(registerBeanName);
            springJobScheduler.init();
            this.logger.info("【" + jobName + "】\t" + jobClass + "\tinit success");
        }
    }

    private List<BeanDefinition> getTargetElasticJobListeners(ElasticJobConf conf) {
        List<BeanDefinition> result = new ManagedList(2);
        String listeners = this.getEnvironmentStringValue(conf.name(), JobAttributeTag.LISTENER, conf.listener());
        if (StringUtils.hasText(listeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
            factory.setScope("prototype");
            result.add(factory.getBeanDefinition());
        }

        String distributedListeners = this.getEnvironmentStringValue(conf.name(), JobAttributeTag.DISTRIBUTED_LISTENER, conf.distributedListener());
        long startedTimeoutMilliseconds = this.getEnvironmentLongValue(conf.name(), JobAttributeTag.DISTRIBUTED_LISTENER_STARTED_TIMEOUT_MILLISECONDS, conf.startedTimeoutMilliseconds());
        long completedTimeoutMilliseconds = this.getEnvironmentLongValue(conf.name(), JobAttributeTag.DISTRIBUTED_LISTENER_COMPLETED_TIMEOUT_MILLISECONDS, conf.completedTimeoutMilliseconds());
        if (StringUtils.hasText(distributedListeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListeners);
            factory.setScope("prototype");
            factory.addConstructorArgValue(startedTimeoutMilliseconds);
            factory.addConstructorArgValue(completedTimeoutMilliseconds);
            result.add(factory.getBeanDefinition());
        }

        return result;
    }

    private String getEnvironmentStringValue(String jobName, String fieldName, String defaultValue) {
        String key = this.prefix + jobName + "." + fieldName;
        String value = this.environment.getProperty(key);
        return StringUtils.hasText(value) ? value : defaultValue;
    }

    private int getEnvironmentIntValue(String jobName, String fieldName, int defaultValue) {
        String key = this.prefix + jobName + "." + fieldName;
        String value = this.environment.getProperty(key);
        return StringUtils.hasText(value) ? Integer.valueOf(value) : defaultValue;
    }

    private long getEnvironmentLongValue(String jobName, String fieldName, long defaultValue) {
        String key = this.prefix + jobName + "." + fieldName;
        String value = this.environment.getProperty(key);
        return StringUtils.hasText(value) ? Long.valueOf(value) : defaultValue;
    }

    private boolean getEnvironmentBooleanValue(String jobName, String fieldName, boolean defaultValue) {
        String key = this.prefix + jobName + "." + fieldName;
        String value = this.environment.getProperty(key);
        return StringUtils.hasText(value) ? Boolean.valueOf(value) : defaultValue;
    }
}
