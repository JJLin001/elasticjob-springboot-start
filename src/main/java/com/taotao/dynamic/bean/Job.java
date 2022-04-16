package com.taotao.dynamic.bean;

import lombok.Data;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/16/18:25
 */
@Data
public class Job {
    private String jobName;
    private String jobType;
    private String jobClass;
    private String cron;
    private int shardingTotalCount = 1;
    private String shardingItemParameters = "";
    private String jobParameter = "";
    private boolean failover = false;
    private boolean misfire = false;
    private String description = "";
    private boolean overwrite = false;
    private boolean streamingProcess = false;
    private String scriptCommandLine = "";
    private boolean monitorExecution = true;
    private int monitorPort = -1;
    private int maxTimeDiffSeconds = -1;
    private String jobShardingStrategyClass = "";
    private int reconcileIntervalMinutes = 10;
    private String eventTraceRdbDataSource = "";
    private String listener = "";
    private boolean disabled = false;
    private String distributedListener = "";
    private long startedTimeoutMilliseconds = 9223372036854775807L;
    private long completedTimeoutMilliseconds = 9223372036854775807L;
    private JobProperties jobProperties = new JobProperties();
}
