package com.furkantufan.courier.tracking.common.async;

import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

    public static final String THREAD_EXECUTOR_NAME = "defaultThreadExecutor";

    @Bean(THREAD_EXECUTOR_NAME)
    public Executor publishTaskExecutor(ThreadProperties properties) {
        var taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(properties.getCorePoolSize());
        taskExecutor.setMaxPoolSize(properties.getMaxPoolSize());
        taskExecutor.setQueueCapacity(properties.getQueueCapacity());
        taskExecutor.setThreadNamePrefix(properties.getName());
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    @ConfigurationProperties(prefix = "thread-properties")
    public ThreadProperties threadProperties() {
        return new ThreadProperties();
    }

    @Override
    public Executor getAsyncExecutor() {
        return new SimpleAsyncTaskExecutor();
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    public static class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            ex.printStackTrace();
        }
    }
}