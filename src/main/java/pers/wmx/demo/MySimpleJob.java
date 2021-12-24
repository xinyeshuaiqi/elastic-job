package pers.wmx.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @author wangmingxin03
 * Created on 2021-07-08
 */
@Component
public class MySimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext context) {
        System.out.println(
                String.format("分片项 ShardingItem: %s | 运行时间: %s | 线程ID: %s | 分片参数: %s | 任务参数: %s ",
                        context.getShardingItem(),
                        new SimpleDateFormat("HH:mm:ss").format(new Date()),
                        Thread.currentThread().getId(),
                        context.getShardingParameter(),
                        context.getJobParameter())
        );
    }
}
