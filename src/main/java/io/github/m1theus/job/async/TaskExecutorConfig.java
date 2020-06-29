package io.github.m1theus.job.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class TaskExecutorConfig {

    public static final String DEFAULT_CORE_TASK_EXECUTOR = "defaultCoreTaskExecutor";

    @Bean(DEFAULT_CORE_TASK_EXECUTOR)
    public TaskExecutor defaultCoreTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.setCorePoolSize(10);
        executor.setQueueCapacity(100);
        executor.initialize();
        return executor;
    }

}
