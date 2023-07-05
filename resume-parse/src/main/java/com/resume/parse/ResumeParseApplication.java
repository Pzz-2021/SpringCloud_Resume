package com.resume.parse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@MapperScan("com.resume.parse.mapper")
@SpringBootApplication
public class ResumeParseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeParseApplication.class, args);
    }

}
