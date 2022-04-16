package com.taotao.autoconfigure;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.activation.DataSource;

/**
 * @Description:
 * @Auther: zhangtao
 * @Date: 2022/04/16/18:06
 */
@Configuration
@EnableConfigurationProperties({ZookeeperProperties.class})
public class JobParserAutoConfiguration {
 @Autowired
 private ZookeeperProperties zookeeperProperties;

 @Bean(initMethod = "init")
 public ZookeeperRegistryCenter zookeeperRegistryCenter() {
  ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(this.zookeeperProperties.getServerLists(), this.zookeeperProperties.getNamespace());
  zkConfig.setBaseSleepTimeMilliseconds(this.zookeeperProperties.getBaseSleepTimeMilliseconds());
  zkConfig.setConnectionTimeoutMilliseconds(this.zookeeperProperties.getConnectionTimeoutMilliseconds());
  zkConfig.setDigest(this.zookeeperProperties.getDigest());
  zkConfig.setMaxRetries(this.zookeeperProperties.getMaxRetries());
  zkConfig.setMaxSleepTimeMilliseconds(this.zookeeperProperties.getMaxSleepTimeMilliseconds());
  zkConfig.setSessionTimeoutMilliseconds(this.zookeeperProperties.getSessionTimeoutMilliseconds());
  return new ZookeeperRegistryCenter(zkConfig);
 }
}
