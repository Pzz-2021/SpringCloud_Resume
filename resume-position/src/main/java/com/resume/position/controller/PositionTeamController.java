package com.resume.position.controller;

import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.Constant;
import com.resume.base.utils.JwtUtil;
import com.resume.dubbo.domian.MemberDTO;
import com.resume.dubbo.domian.PositionTeam;
import com.resume.position.service.PositionTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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


    @ApiOperation(value = "查询职位可选负责人",notes = "已选的除外，可选的包括HR和面试官")
    @GetMapping("/optional-position-team/{positionId}")
    public RestResponse<List<MemberDTO>> optionalPositionTeam(@PathVariable("positionId")Long positionId,HttpServletRequest httpServletRequest) {
        TokenInfo tokenInfo = JwtUtil.getTokenInfo(httpServletRequest);
        List<MemberDTO> optionalPositionTeam = positionTeamService.getOptionalPositionTeam(positionId, tokenInfo.getCompanyId());
        return RestResponse.success(optionalPositionTeam);
    }

    @ApiOperation(value = "查询职位已选负责人",notes = "包括HR和面试官")
    @GetMapping("/selected-position-team/{positionId}")
    public RestResponse<List<PositionTeam>> selectedPositionTeam(@PathVariable("positionId")Long positionId) {
        List<PositionTeam> selectedPositionTeam = positionTeamService.getSelectedPositionTeam(positionId);
        return RestResponse.success(selectedPositionTeam);
    }

    @ApiOperation(value = "添加职位负责人", notes = "记得传头像和用户名")
    @PostMapping("/add-position-team")
    public RestResponse<String> addPositionTeam(@RequestBody PositionTeam positionTeam) {
        if(Constant.HR.equals(positionTeam.getRoleName()))positionTeam.setRoleId(3);
        if(Constant.INTERVIEWER.equals(positionTeam.getRoleName()))positionTeam.setRoleId(4);
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

