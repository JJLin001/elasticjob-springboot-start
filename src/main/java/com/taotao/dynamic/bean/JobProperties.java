package com.taotao.dynamic.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/16/18:28
 */
@Data
public class JobProperties {
    @JsonProperty("job_exception_handler")
    private String jobExceptionHandler = "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";
    @JsonProperty("executor_service_handler")
    private String executorServiceHandler = "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";
}
