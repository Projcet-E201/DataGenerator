package com.example.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);        // 최소 핵심 스레드 수
        executor.setMaxPoolSize(10);        // 동시 실행 가능한 최대 스레드 수
        executor.setQueueCapacity(500);     // 대기 작업의 최대 수
        executor.setThreadNamePrefix("AsyncThread-");   // 생성되는 각 스레드 이름의 접두사
        executor.initialize();
        return executor;
    }
}
