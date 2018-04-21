package com.zhenhui.demo.toptop.follow.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.zhenhui.demo.toptop.follow.dal.repository")
@ComponentScan(basePackages = "com.zhenhui.demo.toptop.follow")
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }

}




