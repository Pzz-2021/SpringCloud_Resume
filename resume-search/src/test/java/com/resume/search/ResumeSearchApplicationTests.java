package com.resume.search;

import com.resume.base.utils.DateUtil;
import com.resume.dubbo.api.SearchService;
import com.resume.dubbo.domian.PositionDTO;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
class ResumeSearchApplicationTests {
    @Autowired
    private SearchService searchService;

    @Test
    void contextLoads() {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setPkPositionId(1L);
        positionDTO.setUpdateTime(DateUtil.getDate2());
        positionDTO.setPositionTeamIdList(new ArrayList<>(Arrays.asList(25L, 31L)));
        searchService.updatePositionDTOById(positionDTO);
    }

}
