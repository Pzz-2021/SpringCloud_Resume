package com.resume.position;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.resume.dubbo.domian.Position;
import com.resume.position.service.PositionService;
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
    private PositionService positionService;

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
    @Test
    void test2(){
        Position position=new Position();
        position.setWorkingCity("上海");
        position.setPkPositionId(1L);
        LambdaUpdateWrapper<Position> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Position::getPkPositionId,2L);
        positionService.update(position,lambdaUpdateWrapper);
    }
}
