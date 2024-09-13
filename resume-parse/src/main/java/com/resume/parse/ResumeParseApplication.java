package com.resume.parse;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.resume.parse.mapper")
@EnableLogRecord(tenant = "ResumeParse")
@SpringBootApplication
public class ResumeParseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeParseApplication.class, args);
    }

}
