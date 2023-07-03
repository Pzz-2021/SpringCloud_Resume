package com.resume.dubbo.api;

import com.resume.base.model.PageBean;
import com.resume.base.model.TokenInfo;
import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.PositionDTO;
import com.resume.dubbo.domian.SearchCondition;

public interface SearchService {

    Boolean savePositionDto(PositionDTO... positionDtos);

    Boolean deletePositionDtoById(Long id);

    Boolean updatePositionDtoById(PositionDTO positionDto);

    Position getPositionById(Long id);

    PageBean<Position> searchPosition(SearchCondition searchCondition, TokenInfo tokenInfo);

}
