# elsaticjob-springboot-start
elasticjob简单封装

 **工程介绍** 
elsaticjob-springboot-start 是封装elasticjob-2.1.5版本的jar
model工程elasticjob-springboot-start-demo 是示例

主要有两种用法，第一种通过注解的方式，第二种数据库配置的方式，也可以写一个新增页面可是支持的。

 **第一种用法以注解的方式** 

1、引入依赖

```
<!-- 我们自己封装的elasticjob -->
<dependency>
        <groupId>com.taotao</groupId>
        <artifactId>elsaticjob-springboot-start</artifactId>
        <version>1.0</version>
</dependency>
​
<!-- 阿里云仓库地址，已经放上去了，不确定能不能下载到，不能还是去gitee上拉去代码 -->
<repositories>
        <repository>
            <id>aliyun</id>
            <name>aliyun</name>
            <url>https://maven.aliyun.com/repository/release</url>
        </repository>
</repositories>
```

2、在resource下新建application.yml文件


```
server:
  port: 8088
elastic-job:
  zk:
    server-lists: 10.211.55.3:2181
    namespace: elasticjob-springboot-start-demo
    max-sleep-time-milliseconds: 3000
    base-sleep-time-milliseconds: 1000
```

3、编写我们的执行器类

```
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
```

 **第二种用法是数据库配置** 

1、执行sql语句，当然这个是简单的字段，具体建表还根据具体的业务情况，如下只是个演示


```
create database elasticjob;
use elasticjob;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `t_job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jobName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称',
  `jobType` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务类型支持三种SIMPLE、DATAFLOW、SCRIPT',
  `jobClass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实现类',
  `cron` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '触发时间',
  `shardingTotalCount` int(10) COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分片数量',
  `shardingItemParameters` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分片别名映射',
  `jobParameter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '任务参数',
  `description` varchar(255) NULL DEFAULT NULL COMMENT '任务描述',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '任务创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '任务更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;
​
insert into t_job values (1,'MyJobDb','SIMPLE','com.taotao.worker.MyJobDb','0/5 * * * * ?',1,'0=A','','第一次数据库配置启动测试',now(),now());
```

​
2、引入相关依赖


```
<dependencies>
        <!-- mysql -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.11</version>
        </dependency>
        <!-- spring web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.4.5</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
            <version>2.4.5</version>
        </dependency>
        <!-- mybatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.4</version>
        </dependency>
        <!-- 我们自己封装的elasticjob -->
        <dependency>
            <groupId>com.taotao</groupId>
            <artifactId>elsaticjob-springboot-start</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
​
    <repositories>
        <repository>
            <id>aliyun</id>
            <name>aliyun</name>
            <url>https://maven.aliyun.com/repository/release</url>
        </repository>
    </repositories>
```

3、修改application.yml


```
server:
  port: 8088
elastic-job:
  zk:
    server-lists: 10.211.55.3:2181
    namespace: elasticjob-springboot-start-demo
    max-sleep-time-milliseconds: 3000
    base-sleep-time-milliseconds: 1000
spring:
  application:
    name: elasticjob-springboot-start-demo # 应用名称
  # 数据库
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: 12345678
    url: jdbc:mysql://127.0.0.1:3306/elasticjob?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false
  mybatis:
    configuration:
      map-underscore-to-camel-case: true # 开启驼峰映射
```

​
4、api调用

```

1、从数据库查出来job信息
2、封装成com.taotao.dynamic.bean.Job对象
import com.taotao.dynamic.service.JobService;
3、调用jobService.addJob(job);
```

如果上面的依赖下载不下来

小伙伴们可以关注我公众号RuntimeException，获取elasticjob学习文档​哦

1、公众号回复maven关键词获取封装好的elasticjob.jar包，解压放到本地。maven仓库com路径下就可以引用到了哈​。

2、公众号回复中文文档关键词就可以获取官方提供的中文学习文档啦​。

