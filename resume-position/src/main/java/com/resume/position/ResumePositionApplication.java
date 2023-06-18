package com.resume.position;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.resume.position.mapper")
@SpringBootApplication
public class ResumePositionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumePositionApplication.class, args);
    }

}
