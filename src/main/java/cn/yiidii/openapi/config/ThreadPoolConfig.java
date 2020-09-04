package cn.yiidii.openapi.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yiidii Wang
 * @desc 线程池配置
 */
@Configuration
@EnableScheduling
@EnableAsync
@Slf4j
public class ThreadPoolConfig {
    /**
     * CPU个数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数（默认线程数）
     */
    private static final int corePoolSize = CPU_COUNT + 1;
    /**
     * 最大线程数
     */
    private static final int maxPoolSize = CPU_COUNT * 2 + 1;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int keepAliveTime = 10;
    /**
     * 缓冲队列大小
     */
    private static final int queueCapacity = 100;
    /**
     * 线程池名前缀
     */
    private static final String chinaUnicomTaskExcutorThreadNamePrefix = "Async-chinaUnicomTaskExcutor-";
    private static final String meituanTaskExcutorThreadNamePrefix = "Async-meituanTask-";

    /**
     * 中国联通相关 线程池配置
     *
     * @return
     */
    @Bean("chinaUnicomTaskExcutor")
    public Executor chinaUnicomTaskExcutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(chinaUnicomTaskExcutorThreadNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());// 由调用线程（提交任务的线程）处理该任务
        executor.initialize();
        return executor;
    }

    /**
     * 美团执行下单相关 线程池配置
     *
     * @return
     */
    @Bean("meituanTaskExcutor")
    public Executor meituanTaskExcutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(meituanTaskExcutorThreadNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());// 由调用线程（提交任务的线程）处理该任务
        executor.initialize();
        return executor;
    }

}
