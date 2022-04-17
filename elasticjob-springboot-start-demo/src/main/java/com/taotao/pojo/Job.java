package com.taotao.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/17/11:24
 */
@Data
@ToString
public class Job {
    private String jobName;
    private String jobType;
    private String jobClass;
    private String cron;
    private int shardingTotalCount;
    private String shardingItemParameters;
    private String jobParameter;
    private String description;
}
