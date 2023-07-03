package com.resume.position;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.PositionDTO;
import com.resume.position.mapstruct.PosistionMapstruct;
import com.resume.position.service.PositionService;
import com.resume.position.utils.RedisData;
import com.resume.position.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
        List<Position> positions = positionService.getPositionMapper().selectAllPosition();
        List<PositionDTO>list=new ArrayList<>();
        positions.parallelStream().forEach(new Consumer<Position>() {
            @Override
            public void accept(Position position) {
                PositionDTO positionDTO= PosistionMapstruct.INSTANCT.conver(position);
                positionDTO.setHrIdList(positionService.getPositionMapper().selectPositionHrId(position.getPkPositionId()));
                positionDTO.setInterviewerIdList(positionService.getPositionMapper().selectPositionInterviewerId(position.getPkPositionId()));
                list.add(positionDTO);
            }
        });
        list.forEach(new Consumer<PositionDTO>() {
            @Override
            public void accept(PositionDTO positionDTO) {
                System.out.println("职位："+positionDTO);
            }
        });
    }
}
