package com.resume.dubbo.api;

import com.resume.dubbo.domian.Position;

public interface SearchService {

    Boolean savePosition(Position... positions);

    Boolean deletePositionById(Long id);

    Boolean updatePositionById(Position position);

    Position getPositionById(Long id);
}
