package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author pengkun shan
 */
@SpringBootApplication
@MapperScan("com.example.demo.mapper")

public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("启动成功！！");
        System.out.println("启动成功！！");
        System.out.println("启动成功！！");
    }
}
