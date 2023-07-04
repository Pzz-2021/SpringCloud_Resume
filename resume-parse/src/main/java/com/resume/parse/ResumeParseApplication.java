package com.resume.parse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.resume.parse.mapper")
@SpringBootApplication
public class ResumeParseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeParseApplication.class, args);
    }

}
