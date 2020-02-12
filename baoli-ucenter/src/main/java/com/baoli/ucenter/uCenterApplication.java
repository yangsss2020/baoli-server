package com.baoli.ucenter;

import com.baoli.ucenter.netty.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
@Slf4j
public class uCenterApplication implements CommandLineRunner {
    @Autowired
    private WebSocketServer websocketServer;
    public static void main(String[] args) {
        SpringApplication.run(uCenterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("服务器启动");
            System.out.println("服务器启动");
            websocketServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
