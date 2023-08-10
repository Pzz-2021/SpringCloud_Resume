package com.resume.position;

import com.resume.dubbo.api.SearchService;
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

@SpringBootTest
class ResumePositionApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PositionService positionService;

    @Autowired
    private SearchService searchService;


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
    void test0() {
        System.out.println(redisUtil.sSet("cache:check-resume:123", "value1"));

        boolean result = redisUtil.sHasKey("cache:check-resume:123", "value1");

        System.out.println(result);
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

    // 全量保存
    @Test
    void synchronizationEsForAll() {
        List<Position> positions = positionService.getPositionMapper().selectAllPosition();

        List<PositionDTO> list = new ArrayList<>();
        positions.parallelStream().forEach(position -> {
            PositionDTO positionDTO = PosistionMapstruct.INSTANCT.conver(position);
            positionDTO.setPositionTeamIdList(positionService.getPositionMapper().selectPositionTeam(position.getPkPositionId()));
            list.add(positionDTO);
        });

       list.forEach(positionDTO -> {
           System.out.println(positionDTO);
           System.out.println();
       });

        searchService.savePositionDTOs(list);
    }
}
