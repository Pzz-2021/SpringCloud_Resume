package com.resume.search.mapstruct;

import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.PositionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PositionMapper {
    PositionMapper INSTANCE = Mappers.getMapper(PositionMapper.class);


    Position convertToPosition(PositionDto user);
}
