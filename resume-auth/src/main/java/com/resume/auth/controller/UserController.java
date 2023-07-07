package com.resume.auth.controller;

import com.resume.dubbo.domian.MemberDTO;
import com.resume.auth.dto.UserInfoDTO;
import com.resume.auth.mapstruct.UserMapstruct;
import com.resume.auth.pojo.Company;
import com.resume.auth.pojo.User;
import com.resume.auth.service.UserService;
import com.resume.auth.utils.SM3Util;
import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
 *@filename: UserController
 *@author: lyh
 *@date:2023/7/2 16:31
 *@version 1.0
 *@description TODO
 */

@RestController
@Api(tags = "个人设置接口")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "修改个人信息",notes = "要包含userId")
    @PutMapping("/edit-personal-message")
    public RestResponse<String> editPersonalMessage(@RequestBody UserInfoDTO userInfoDTO) {
         User user=UserMapstruct.INSTANCT.conver(userInfoDTO);
         boolean save=userService.editPersonalMessage(user);
         //TODO 需要修改冗余的其他的表
         return RestResponse.judge(save);
    }
    @ApiOperation(value = "查询公司信息")
    @GetMapping("/get-company-message")
    public RestResponse<Company> getCompanyMessage(HttpServletRequest httpServletRequest) {
        TokenInfo tokenInfo=JwtUtil.getTokenInfo(httpServletRequest);
        Company company=userService.getCompanyMessage(tokenInfo.getCompanyId());
        return RestResponse.success(company);
    }
    @ApiOperation(value = "修改公司信息")
    @PutMapping("/edit-company-message")
    public RestResponse<String> editPersonalMessage(@RequestBody Company company) {
        boolean save=userService.editCompanyMessage(company);
        return RestResponse.judge(save);
    }
    @ApiOperation(value = "查询团队成员")
    @GetMapping("/select-team-members")
    public RestResponse< List<MemberDTO>> selectTeamMembers(HttpServletRequest httpServletRequest) {
        TokenInfo tokenInfo=JwtUtil.getTokenInfo(httpServletRequest);
        List<MemberDTO> teamMembers = userService.selectTeamMembers(tokenInfo.getCompanyId());
        return RestResponse.success(teamMembers);
    }
    @ApiOperation(value = "添加团队成员",notes = "可添加角色为：HR 和 面试官")
    @PostMapping("/add-team-members")
    public RestResponse<String> addTeamMembers(HttpServletRequest httpServletRequest,@RequestBody MemberDTO memberDTO) {
        TokenInfo tokenInfo=JwtUtil.getTokenInfo(httpServletRequest);
        boolean save=userService.addTeamMembers(memberDTO,tokenInfo.getCompanyId());
        if(save)return RestResponse.success();
        else return RestResponse.error("邮箱重复!请再次检查!");
    }
    @ApiOperation(value = "删除团队成员",notes = "传要删除的成员(公司管理员不能互删)：userId")
    @DeleteMapping("/delete-team-members/{userId}")
    public RestResponse<String> deleteTeamMembers(@PathVariable("userId") Long targetUserId) {
        boolean save=userService.deleteTeamMembers(targetUserId);
        if(save)return RestResponse.success();
        else return RestResponse.error("权限不足!");
    }
}
