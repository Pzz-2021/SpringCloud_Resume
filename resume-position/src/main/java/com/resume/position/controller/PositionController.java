package com.resume.position.controller;

import com.resume.base.model.PageBean;
import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.Constant;
import com.resume.base.utils.DateUtil;
import com.resume.base.utils.JwtUtil;
import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.SearchCondition;
import com.resume.position.pojo.PositionTeam;
import com.resume.position.service.PositionService;
import com.resume.position.service.PositionTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "职位接口")
public class PositionController {
    @Autowired
    private PositionService positionService;

    @ApiOperation(value = "添加职位", notes = "前端将创建人的名字和头像传过来")
    @PostMapping("/add-position")
    public RestResponse<String> addPosition(HttpServletRequest httpServletRequest, @RequestBody Position position) {
        TokenInfo tokenInfo = JwtUtil.getTokenInfo(httpServletRequest);
        boolean save = positionService.addPosition(tokenInfo, position);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "编辑职位", notes = "传positionId")
    @PutMapping("/edit-position")
    public RestResponse<String> selectPositionByPage(@RequestBody Position position) {
        boolean save = positionService.editPosition(position);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "关闭职位", notes = "传positionId")
    @PutMapping("/close-position")
    public RestResponse<String> closePosition(Long positionId) {
        boolean save = positionService.closePosition(positionId);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "开启职位", notes = "传positionId")
    @PutMapping("/open-position")
    public RestResponse<String> openPosition(Long positionId) {
        boolean save = positionService.openPosition(positionId);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "分页查询职位", notes = "不同角色查询的岗位不同")
    @PostMapping("/select-position/by-page")
    public RestResponse<PageBean<Position>> selectPositionByEs(HttpServletRequest httpServletRequest, @RequestBody SearchCondition searchCondition) {
        TokenInfo tokenInfo = JwtUtil.getTokenInfo(httpServletRequest);
        tokenInfo = new TokenInfo(1L, 1L, "超级管理员");
        PageBean<Position> positionPageBean = positionService.selectPositionByEs(searchCondition, tokenInfo);

        System.out.println("----------------------------");
        System.out.println(positionPageBean);
        System.out.println("----------------------------");

        return RestResponse.success(positionPageBean);
    }

    @ApiOperation(value = "分页查询职位（测试）", notes = "不同角色查询的岗位不同")
    @PostMapping("/select-position/by-page-test")
    public RestResponse<PageBean<Position>> selectPositionByEs(@RequestBody SearchCondition searchCondition) {
        TokenInfo tokenInfo = new TokenInfo(1L, 1L, "超级管理员");
        PageBean<Position> positionPageBean = positionService.selectPositionByEs(searchCondition, tokenInfo);

        System.out.println("----------------------------");
        System.out.println(positionPageBean);
        System.out.println("----------------------------");

        return RestResponse.success(positionPageBean);
    }

//    @ApiOperation(value = "分页查询职位", notes = "不同角色查询的岗位不同")
//    @GetMapping("/select-positionByPage")
//    public RestResponse<PageBean<Position>> selectPositionByPage(HttpServletRequest httpServletRequest, @RequestParam(defaultValue = "1") int nowPage) {
//        TokenInfo tokenInfo = JwtUtil.getTokenInfo(httpServletRequest);
//        PageBean<Position> positionPageBean = positionService.selectPositionByPage(tokenInfo, nowPage);
//        return RestResponse.success(positionPageBean);
//    }
}

