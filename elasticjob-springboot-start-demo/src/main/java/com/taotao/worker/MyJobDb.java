package com.taotao.worker;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/17/12:47
 */
public class MyJobDb implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        int shardingItem = shardingContext.getShardingItem();
        String shardingParameter = shardingContext.getShardingParameter();
        System.out.println("测试第一次db配置方式>>>"+shardingItem+">>>>"+"分片>>>>>"+shardingParameter);
    }
}
