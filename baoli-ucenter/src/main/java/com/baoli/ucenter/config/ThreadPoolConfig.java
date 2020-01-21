package com.baoli.ucenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ys
 * @create 2020-01-18-20:01
 */
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ExecutorService threadPoolExecutor() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        return executorService;
    }
}
