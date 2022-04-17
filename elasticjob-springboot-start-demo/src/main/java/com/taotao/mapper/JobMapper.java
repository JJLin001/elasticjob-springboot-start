package com.taotao.mapper;

import com.taotao.pojo.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/17/11:25
 */
@Mapper
public interface JobMapper {
    @Select("select id, jobName, jobType, jobClass, cron, shardingTotalCount, shardingItemParameters, jobParameter, description, create_date, update_date from t_job ")
    List<Job> selectJob();
}
