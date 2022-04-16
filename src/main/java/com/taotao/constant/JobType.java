package com.taotao.constant;

import lombok.Data;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/16/18:11
 */

public enum JobType {

 SimpleJob("SimpleJob", "Simple类型作业"),
 DataflowJob("DataflowJob", "Dataflow类型作业"),
 ScriptJob("ScriptJob", "Script类型作业");

 private String name;
 private String description;

 private JobType(String name, String description) {
  this.name = name;
  this.description = description;
 }

 public String getName() {
  return this.name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getDescription() {
  return this.description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

}
