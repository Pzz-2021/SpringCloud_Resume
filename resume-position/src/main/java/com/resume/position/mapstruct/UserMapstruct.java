package com.resume.position.mapstruct;

import com.resume.dubbo.domian.MemberDTO;
import com.resume.position.pojo.PositionTeam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/*
 *@filename: PosistionMapstruct
 *@author: pp
 *@date:2023/7/5 9:14
 *@version 1.0
 *@description TODO
 */
@Mapper
public interface UserMapstruct {
    UserMapstruct INSTANCE = Mappers.getMapper(UserMapstruct.class);

    @Mappings({
            @Mapping(source = "pkUserId", target = "userId"),
            @Mapping(source = "accountPicture", target = "userPicture")
    })
    PositionTeam convert(MemberDTO position);
}
