package com.taotao.worker;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.taotao.constant.JobType;
import com.taotao.nanotation.ElasticJobConf;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/17/11:16
 */
@ElasticJobConf(type = JobType.SimpleJob, name = "Myjob",
        cron = "0/5 * * * * ?", shardingTotalCount = 1, shardingItemParameters = "0=A",
        description = "第一次注解测试")
public class Myjob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        int shardingItem = shardingContext.getShardingItem();
        String shardingParameter = shardingContext.getShardingParameter();
        System.out.println("测试注解方式>>分片>>"+shardingItem+">>>>"+"分片参数>>"+shardingParameter);
    }
}
