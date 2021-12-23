package pers.wmx.demo;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.purgeteam.elasticjob.starter.util.ElasticJobUtils;

/**
 * @author wangmingxin03
 * Created on 2021-12-23
 */
@Configuration
public class MyJobConfig {
    // job 名称
    private static final String JOB_NAME = "MySimpleJob";

    // 定时器cron参数
    private static final String CRON = "0/5 * * * * ?";

    // 定时器分片
    private static final int SHARDING_TOTAL_COUNT = 2;

    // 分片参数
    private static final String SHARDING_ITEM_PARAMETERS = "0=Beijing,1=Shanghai";

    // 自定义参数
    private static final String JOB_PARAMETERS = "parameter";

    @Resource
    private ZookeeperRegistryCenter regCenter;

    @Resource
    private JobEventConfiguration jobEventConfiguration;

    // Spring Boot 整合跑起来
    @Bean(initMethod = "init")
    public JobScheduler mySimpleJobScheduler(final MySimpleJob mySimpleJob) {

        LiteJobConfiguration liteJobConfiguration = ElasticJobUtils
                .getLiteJobConfiguration(mySimpleJob.getClass(), JOB_NAME, CRON,
                        SHARDING_TOTAL_COUNT, SHARDING_ITEM_PARAMETERS, JOB_PARAMETERS);
        // 参数：1.定时器实例，2.注册中心类，3.LiteJobConfiguration，
        //     3.历史轨迹（不需要可以省略）
        return new SpringJobScheduler(mySimpleJob, regCenter, liteJobConfiguration, jobEventConfiguration);
    }
}
