package com.resume.position;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@MapperScan("com.resume.position.mapper")
@SpringBootApplication
public class ResumePositionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumePositionApplication.class, args);
    }

}
