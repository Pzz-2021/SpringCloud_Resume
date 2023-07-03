package com.resume.auth.mapstruct;

import com.resume.auth.dto.EnrollDTO;
import com.resume.auth.dto.MemberDTO;
import com.resume.auth.dto.UserInfoDTO;
import com.resume.auth.pojo.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/*
 *@filename: UserMapper
 *@author: lyh
 *@date:2023/6/12 8:37
 *@version 1.0
 *@description TODO
 */
@Mapper
public interface UserMapstruct {
    UserMapstruct INSTANCT = Mappers.getMapper(UserMapstruct.class);

    //返回的是目标对象
    UserInfoDTO conver(User user);

    User conver(UserInfoDTO userInfoDTO);

    User conver(EnrollDTO enrollDTO);

    User conver(MemberDTO memberDTO);

}
