package com.resume.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.auth.mapper.UserMapper;
import com.resume.auth.pojo.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public User login(User user) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserEmail,user.getUserEmail()).eq(User::getPassword,user.getPassword());
        return getOne(queryWrapper);
    }

}
