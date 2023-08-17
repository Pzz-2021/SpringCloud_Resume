package com.resume.position.mapper;

import com.resume.dubbo.domian.Position;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.dubbo.domian.ResumeStateDTO;
import com.resume.position.dto.CVTrendDTO;
import com.resume.position.dto.InterviewTrendDTO;
import com.resume.position.dto.OfferTrendDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 彭政
 * @since 2023-06-18
 */
public interface PositionMapper extends BaseMapper<Position> {
    List<Position> selectPositionByAdmin(@Param("companyId") Long companyId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    int totalCountPositionByAdmin(@Param("companyId") Long companyId);

    List<Position> selectPositionByHr(@Param("userId") Long userId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    int totalCountPositionByHr(@Param("userId") Long userId);

    List<Position> selectPositionByInterviewer(@Param("userId") Long userId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    int totalCountPositionByInterviewer(@Param("userId") Long userId);

    int changePositionResumeCount(ResumeStateDTO resumeStateDTO);

    void addCandidateNum(Long positionId);

    void decreaseCandidateNum(@Param("positionId")Long positionId,@Param("preState")String preState);

    int getCandidateNum(Long positionId);

    CVTrendDTO getCVTrend(@Param("companyId")String companyId);

    InterviewTrendDTO getInterviewTrend(@Param("companyId")String companyId);

    OfferTrendDTO getOfferTrend(@Param("companyId")String companyId);

    //ES数据同步测试用
    List<Position> selectAllPosition();

    List<Long> selectPositionTeam(@Param("positionId") Long positionId);

}
