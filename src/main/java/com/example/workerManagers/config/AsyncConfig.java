package com.example.workerManagers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.setTaskDecorator(new SecurityContextDecorator());
        
        // 에러 핸들러 설정
        executor.setRejectedExecutionHandler((r, e) -> {
            log.error("Task rejected, thread pool is full");
            throw new RuntimeException("Server is too busy, please try again later");
        });
        
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.error("Async method {} threw exception: {}", method.getName(), ex.getMessage(), ex);
            }
        };
    }

    @Slf4j
    public static class SecurityContextDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            return () -> {
                try {
                    SecurityContextHolder.setContext(securityContext);
                    log.debug("Set security context in async thread");
                    runnable.run();
                } catch (Exception e) {
                    log.error("Error in async task: {}", e.getMessage(), e);
                    throw e;
                } finally {
                    SecurityContextHolder.clearContext();
                    log.debug("Cleared security context in async thread");
                }
            };
        }
    }
} 