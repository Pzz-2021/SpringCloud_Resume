package com.resume.position.mapstruct;

import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.PositionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/*
 *@filename: PosistionMapstruct
 *@author: lyh
 *@date:2023/7/3 9:14
 *@version 1.0
 *@description TODO
 */
@Mapper
public interface PosistionMapstruct {
    PosistionMapstruct INSTANCT = Mappers.getMapper(PosistionMapstruct.class);

    PositionDTO conver(Position position);
}
