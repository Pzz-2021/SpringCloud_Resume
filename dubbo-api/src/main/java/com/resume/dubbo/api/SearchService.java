package com.resume.dubbo.api;

import com.resume.base.model.PageBean;
import com.resume.base.model.TokenInfo;
import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.PositionDto;
import com.resume.dubbo.domian.SearchCondition;

import java.util.List;

public interface SearchService {

    Boolean savePositionDto(PositionDto... positionDtos);

    Boolean deletePositionDtoById(Long id);

    Boolean updatePositionDtoById(PositionDto positionDto);

    Position getPositionById(Long id);

    PageBean<Position> searchPosition(SearchCondition searchCondition, TokenInfo tokenInfo);

}
