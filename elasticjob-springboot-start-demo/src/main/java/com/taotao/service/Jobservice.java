package com.taotao.service;

import com.taotao.dynamic.service.JobService;
import com.taotao.mapper.JobMapper;
import com.taotao.pojo.Job;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/17/11:17
 */
@Service
public class Jobservice {

    @Autowired
    JobMapper jobMapper;

    @Autowired
    JobService jobService;

    public void add(Job job) {
        com.taotao.dynamic.bean.Job job1 = new com.taotao.dynamic.bean.Job();
        //属性copy
        BeanUtils.copyProperties(job,job1);
        jobService.addJob(job1);
    }

    public void addAll(){
        //查询数据库配置
        List<Job> jobs = jobMapper.selectJob();
        for (Job jobBean:jobs) {
            //转换成封装的job
            com.taotao.dynamic.bean.Job job1 = new com.taotao.dynamic.bean.Job();
            BeanUtils.copyProperties(jobBean,job1);
            jobService.addJob(job1);
        }
    }
}
