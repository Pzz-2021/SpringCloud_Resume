package com.resume.position.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.position.pojo.PositionTeam;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyh
 * @since 2023-07-03
 */
public interface PositionTeamMapper extends BaseMapper<PositionTeam> {
    List<PositionTeam> getSelectedPositionTeam(Long positionId);

    List<Long>getSelectedPositionTeamMemberId(Long positionId);


}
