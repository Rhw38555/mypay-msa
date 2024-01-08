package com.mypay.taskconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mypay.common","com.mypay.taskconsumer"})
public class TaskConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskConsumerApplication.class, args);
    }
}
