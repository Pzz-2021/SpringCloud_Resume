package com.resume.position.mapstruct;

import org.mapstruct.Mapper;
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
}
