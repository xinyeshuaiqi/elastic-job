package pers.wmx.demo;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

/**
 * @author wangmingxin03
 * Created on 2021-07-08
 */
public class SimpleJobTest {
    public static void main(String[] args) {
        // zk 注册中心
        CoordinatorRegistryCenter registryCenter = new ZookeeperRegistryCenter(
                new ZookeeperConfiguration("39.97.47.254:2181", "ejob-standalone")
        );
        registryCenter.init();

        // 数据源 , 事件执行持久化策略
        DruidDataSource dataSource =new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://39.97.47.254:3306/mydb?useUnicode=true&characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("wmx123");
        JobEventConfiguration jobEventConfig = new JobEventRdbConfiguration(dataSource);

        JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder(
                "simpleJob", "0/20 * * * * ?", 4)
                .build();

        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(
                coreConfig, MySimpleJob.class.getCanonicalName()
        );

        // 作业分片策略
        // 平均分配
        String jobShardingClass = AverageAllocationJobShardingStrategy.class.getCanonicalName();

        // 作业根配置
        LiteJobConfiguration jobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfiguration)
                .overwrite(true)
                .build();

        // 构建job
        new JobScheduler(registryCenter, jobRootConfig, jobEventConfig).init();

    }
}
