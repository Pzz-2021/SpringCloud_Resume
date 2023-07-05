package com.resume.auth.controller;

import com.resume.auth.dto.EnrollDTO;
import com.resume.auth.dto.LoginDTO;
import com.resume.auth.dto.TokenDTO;
import com.resume.auth.mapper.RoleMapper;
import com.resume.auth.mapstruct.CompanyMapstruct;
import com.resume.auth.mapstruct.UserMapstruct;
import com.resume.auth.pojo.Company;
import com.resume.auth.pojo.User;
import com.resume.auth.service.CompanyService;
import com.resume.auth.service.UserService;
import com.resume.auth.utils.SM3Util;
import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/*
 *@filename: AuthManagerController
 *@author: lyh
 *@date:2023/6/11 21:05
 *@version 1.0
 *@description TODO
 */
@Slf4j
@RestController
@Api(tags = "认证接口")
public class AuthManagerController {
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private RoleMapper roleMapper;

    @ApiOperation("登录认证")
    @PostMapping("/login")
    public RestResponse<LoginDTO> login(@RequestBody User user) {
        User result = userService.login(user);
        if (result == null) return RestResponse.error("请检查邮箱或密码");
        else {
            long start = System.currentTimeMillis();
            if (!result.getPassword().equals(SM3Util.encryptPassword(user.getPassword()))) return RestResponse.error("请检查邮箱或密码");
            LoginDTO loginDTO = userService.getPermissions(result.getPkUserId());
            loginDTO.setUserInfoDTO(UserMapstruct.INSTANCT.conver(user));
            log.info("获取权限 耗时：" + (System.currentTimeMillis() - start));
            long start1 = System.currentTimeMillis();
            // 获取  Access_token 和  Refresh_token
            //查询用户角色
            String role = roleMapper.selectUserRole(result.getPkUserId());
            loginDTO.setAccess_token(JwtUtil.createAccessToken(result.getPkUserId(), result.getCompanyId(),role));
            loginDTO.setRefresh_token(JwtUtil.createRefreshToken(result.getPkUserId(), result.getCompanyId(),role));
            log.info("生成Token 耗时：" + (System.currentTimeMillis() - start1));
            return RestResponse.success(loginDTO);
        }
    }

    @PostMapping("/register")
    @ApiOperation("注册接口")
    public RestResponse<String> register(@RequestBody EnrollDTO enrollDTO) {
        if (userService.checkUserEmailIsExist(enrollDTO.getUserEmail())) {
            enrollDTO.setPassword(SM3Util.encryptPassword(enrollDTO.getPassword()));
            Company company = CompanyMapstruct.INSTANCT.conver(enrollDTO);
            companyService.save(company);
            User user = UserMapstruct.INSTANCT.conver(enrollDTO);
            user.setCompanyId(company.getPkCompanyId());
            userService.save(user);
            //赋予角色
            roleMapper.addCompanyAdmin(user.getPkUserId());
            return RestResponse.success();
        }
        else return RestResponse.error("邮箱已注册");
    }

    @ApiOperation("刷新Token")
    @PostMapping("/refresh-token")
    private RestResponse<TokenDTO> refreshToken(HttpServletRequest httpServletRequest) {
        TokenDTO tokenDTO = new TokenDTO();
        TokenInfo tokenInfo=JwtUtil.getTokenInfo(httpServletRequest);
        Long userId = tokenInfo.getPkUserId();
        Long companyId = tokenInfo.getCompanyId();
        String role = tokenInfo.getRole();
        tokenDTO.setAccess_token(JwtUtil.createAccessToken(userId, companyId,role));
        tokenDTO.setRefresh_token(JwtUtil.createRefreshToken(userId, companyId,role));
        return RestResponse.success(tokenDTO);
    }


}
