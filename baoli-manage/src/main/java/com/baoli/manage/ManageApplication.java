package com.baoli.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ys
 * @create 2020-01-08-19:31
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.baoli.common","com.baoli.manage"})
public class ManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
    }
}
