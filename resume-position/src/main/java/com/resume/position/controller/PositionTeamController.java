package com.resume.position.controller;

import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.JwtUtil;
import com.resume.position.pojo.PositionTeam;
import com.resume.position.service.PositionTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @ApiOperation(value = "添加职位负责人")
    @PostMapping("/add-position-team")
    public RestResponse<String> addPositionTeam(@RequestBody PositionTeam positionTeam) {
        boolean save = positionTeamService.addPositionTeam(positionTeam);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "删除一个职位负责人", notes = "前端传职位Id和要删除的用户Id")
    @DeleteMapping("/delete-position-team")
    public RestResponse<String> deletePositionTeam(Long positionId, Long userId) {
        boolean b = positionTeamService.deletePositionTeam(positionId, userId);
        return RestResponse.judge(b);
    }


}

