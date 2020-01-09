package com.baoli.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ys
 * @create 2020-01-09-0:28
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.baoli.common","com.baoli.main"})
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
