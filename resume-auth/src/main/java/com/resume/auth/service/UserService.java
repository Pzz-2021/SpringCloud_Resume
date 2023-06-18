package com.resume.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.auth.dto.LoginDTO;
import com.resume.auth.mapper.UserMapper;
import com.resume.auth.pojo.Operation;
import com.resume.auth.pojo.User;
import com.resume.base.utils.Constant;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 *@filename: UserService
 *@author: lyh
 *@date:2023/6/11 23:20
 *@version 1.0
 *@description TODO
 */
@Service
@Getter
public class UserService extends ServiceImpl<UserMapper, User> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public User login(User user) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserEmail,user.getUserEmail());
        return getOne(queryWrapper);
    }
    public boolean checkUserEmailIsExist(String userEmail) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        //防止重复注册
        queryWrapper.eq(User::getUserEmail,userEmail).eq(User::getIsDeleted,0);
        User result =getOne(queryWrapper);
        return result == null;
    }
    public LoginDTO getPermissions(Long userId){
        LoginDTO loginDTO=new LoginDTO();
        List<Operation> userPermissions = userMapper.getUserPermissions(userId);
        if(userPermissions!=null&&userPermissions.size()>0){
            //将用户对应的权限传给前端，控制具体按钮是否存在
            loginDTO.setPermissionsList(userPermissions.stream().map(Operation::getOperationCode).collect(Collectors.toList()));
            //将用户对应的权限缓存，给后端网关使用的:Method+InterfaceUrl
            List<String>operation=userPermissions.stream().map((resource-> resource.getMethod()+resource.getInterfaceUrl())).collect(Collectors.toList());
            //权限存储时间跟access_token一样长
            stringRedisTemplate.opsForList().leftPushAll(Constant.USER_KEY+userId, operation.toArray(new String[0]));
            stringRedisTemplate.expire(Constant.USER_KEY+userId, Constant.USER_TTL, TimeUnit.HOURS);
        }
        //菜单权限
        loginDTO.setMenusList(userMapper.getMenus(userId));
        return loginDTO;
    }
}
