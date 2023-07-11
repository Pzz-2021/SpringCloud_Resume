package com.resume.parse.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.parse.mapper.InterviewMapper;
import com.resume.parse.pojo.Interview;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-07-04
 */
@Service
public class InterviewService extends ServiceImpl<InterviewMapper, Interview>  {
     @Resource
     private InterviewMapper interviewMapper;

     public List<Interview>queryInterviewByUserId(Long userId){
         return interviewMapper.queryInterviewByUserId(userId);
     }

     public List<Interview>queryInterviewByCompanyId(Long companyId){
         return interviewMapper.queryInterviewByCompanyId(companyId);
     }

    public List<Interview>queryInterviewByResume(Long resumeId){
         return interviewMapper.queryInterviewByResume(resumeId);
    }
}
