package com.resume.auth.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.resume.auth.dto.LoginDTO;
import com.resume.auth.mapstruct.UserMapper;
import com.resume.auth.pojo.Operation;
import com.resume.auth.pojo.User;
import com.resume.auth.service.UserService;
import com.resume.base.model.RestResponse;
import com.resume.base.utils.Constant;
import com.resume.base.utils.RedisConstant;
import com.resume.base.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 *@filename: AuthManagerController
 *@author: lyh
 *@date:2023/6/11 21:05
 *@version 1.0
 *@description TODO
 */
@RestController
@Api(tags = "认证接口")
public class AuthManagerController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation("登录认证")
    @PostMapping("/login")
    public RestResponse<LoginDTO>login(@RequestBody User user){
        User result = userService.login(user);
        if (result == null) return RestResponse.error("登录失败");
        else{
            LoginDTO loginDTO=new LoginDTO();
            loginDTO.setUserInfoDTO(UserMapper.INSTANCT.conver(user));
            // 获取  Access_token 和  Refresh_token
            loginDTO.setAccess_token(JwtUtil.createAccessToken(user.getPkUserId(),user.getCompanyId()));
            loginDTO.setRefresh_token(JwtUtil.createRefreshToken());
            List<Operation> userPermissions = userService.getUserMapper().getUserPermissions(user.getPkUserId());
            if(userPermissions!=null&&userPermissions.size()>0){
                //将用户对应的权限传给前端，控制具体按钮是否存在
                loginDTO.setPermissionsList(userPermissions.stream().map(Operation::getOperationCode).collect(Collectors.toList()));
                //将用户对应的权限缓存，给后端网关使用的:Method+InterfaceUrl
                List<String>operation=userPermissions.stream().map((resource-> resource.getMethod()+resource.getInterfaceUrl())).collect(Collectors.toList());
                //权限存储时间跟access_token一样长
                stringRedisTemplate.opsForList().leftPushAll(Constant.USER_KEY+user.getPkUserId(), operation.toArray(new String[0]));
                stringRedisTemplate.expire(Constant.USER_KEY+user.getPkUserId(), 24, TimeUnit.HOURS);
            }
            return RestResponse.success();
        }
    }
}
