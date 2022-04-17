package com.taotao.controller;

import com.taotao.pojo.Job;
import com.taotao.service.Jobservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/17/11:07
 */
@Controller
@RequestMapping("/job")
public class JobController {
    @Autowired
    Jobservice jobservice;

    @PostMapping("add")
    public void addJob(Job job){
        jobservice.add(job);
    }

    @GetMapping("addAll")
    public void addAll(){
        jobservice.addAll();
    }
}
