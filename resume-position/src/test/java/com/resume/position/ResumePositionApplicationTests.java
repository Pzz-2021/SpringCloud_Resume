package com.resume.position;

import com.resume.position.pojo.Position;
import com.resume.position.utils.RedisData;
import com.resume.position.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ResumePositionApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IPositionService positionService;

    @Test
    void test() {
        Position position = positionService.getById(1L);

        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(1000);
        System.out.println("expireTime = " + expireTime.toString());
        redisUtil.set("cache:position:1", new RedisData(position, expireTime));

        Object v2 = redisUtil.get("cache:position:1");
        System.out.println("v2 = " + v2);
    }


    @Test
    void contextLoads() {
    }

}
