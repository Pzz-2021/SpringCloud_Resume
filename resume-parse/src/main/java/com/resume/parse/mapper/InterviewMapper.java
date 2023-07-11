package com.resume.parse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.parse.pojo.Interview;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyh
 * @since 2023-07-04
 */
public interface InterviewMapper extends BaseMapper<Interview> {

     List<Interview>queryInterviewByUserId(@Param("userId") Long userId);

     List<Interview>queryInterviewByCompanyId(@Param("companyId")Long companyId);

     List<Interview>queryInterviewByResume(@Param("resumeId")Long resumeId);

}
