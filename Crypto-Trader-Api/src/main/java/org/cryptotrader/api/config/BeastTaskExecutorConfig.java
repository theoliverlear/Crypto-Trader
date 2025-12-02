package org.cryptotrader.api.config;

//=================================-Imports-==================================
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Profile("beast")
public class BeastTaskExecutorConfig {
    //==============================-Beans-===================================

    //---------------------Thread-Pool-Task-Executor--------------------------
    @Bean(name = "taskExecutor")
    public TaskExecutor beastThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(36);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(5000);
        executor.setThreadNamePrefix("Beast-");
        executor.initialize();
        return executor;
    }
}
