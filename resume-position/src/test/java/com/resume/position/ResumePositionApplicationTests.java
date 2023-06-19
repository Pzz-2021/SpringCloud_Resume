package com.resume.position;

import com.baomidou.mybatisplus.extension.api.R;
import com.resume.position.pojo.Position;
import com.resume.position.pojo.Remark;
import com.resume.position.service.IPositionService;
import com.resume.position.utils.CacheClient;
import com.resume.position.utils.RedisData;
import com.resume.position.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SpringBootTest
class ResumePositionApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private IPositionService positionService;

    @Test
    void test() {
        Position position = positionService.getById(1L);

        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(1000);
        System.out.println("expireTime = " + expireTime.toString());
        redisUtil.set("cache:position:1", Instant.now().getEpochSecond() + 10);

        Object v2 = redisUtil.get("cache:position:1");
        System.out.println("v2 = " + v2);
    }


    @Test
    void contextLoads() {
        Position position = positionService.getById(1L);

        // 建缓存
        String key = "cache:position:1";
        RedisData value = new RedisData(position, Instant.now().getEpochSecond());
        redisUtil.set(key, value);


    }

    @Test
    void test1() {


        long seconds = Instant.now().getEpochSecond(); // 获取当前秒数
        System.out.println(seconds);

        System.out.println(Instant.now().getEpochSecond());

        // 将秒数转换为LocalDateTime对象
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);
        System.out.println(dateTime);

    }


}
