package com.resume.position.controller;

import com.resume.base.model.PageBean;
import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.JwtUtil;
import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.SearchCondition;
import com.resume.position.service.PositionService;
import com.resume.position.service.PositionTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private PositionTeamService positionTeamService;

//    @ApiOperation(value = "添加职位", notes = "前端将创建人的名字和头像传过来")
//    @PostMapping("/add-position")
//    public RestResponse<String> addPosition(HttpServletRequest httpServletRequest, @RequestBody Position position) {
//
//    }

}

