package com.resume.search.controller;

import com.resume.base.model.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 彭政
 * @since 2023-06-18
 */
@RestController
@Api(tags = "管理职位负责成员接口")
public class PositionTeamController {


    @ApiOperation(value = "查询职位Hr负责人")
    @GetMapping("/test")
    public String test() {
        System.out.println(123);
        return "1111";
    }
}

