package com.baoli.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ys
 * @create 2020-01-09-0:36
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.baoli.common", "com.baoli.ucenter"})
@EnableFeignClients
public class uCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(uCenterApplication.class, args);
    }
}
