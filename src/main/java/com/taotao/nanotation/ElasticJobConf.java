package com.taotao.nanotation;


import com.taotao.constant.JobType;
import org.springframework.stereotype.Component;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/16/17:58
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticJobConf {
    JobType type();

    String name();

    String cron() default "";

    int shardingTotalCount() default 1;

    String shardingItemParameters() default "";

    String jobParameter() default "";

    boolean failover() default false;

    boolean misfire() default false;

    String description() default "";

    boolean overwrite() default false;

    boolean streamingProcess() default false;

    String scriptCommandLine() default "";

    boolean monitorExecution() default true;

    int monitorPort() default -1;

    int maxTimeDiffSeconds() default -1;

    String jobShardingStrategyClass() default "";

    int reconcileIntervalMinutes() default 10;

    String eventTraceRdbDataSource() default "";

    String listener() default "";

    boolean disabled() default false;

    String distributedListener() default "";

    long startedTimeoutMilliseconds() default 9223372036854775807L;

    long completedTimeoutMilliseconds() default 9223372036854775807L;

    String jobExceptionHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    String executorServiceHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";
}
