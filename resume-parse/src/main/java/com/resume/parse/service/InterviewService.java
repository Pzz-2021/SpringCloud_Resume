package com.resume.parse.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.parse.dto.ScheduleInterviewDTO;
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

     public ScheduleInterviewDTO queryInterviewByUserId(Long userId){
         ScheduleInterviewDTO scheduleInterviewDTO=new ScheduleInterviewDTO();
         scheduleInterviewDTO.setYesterdayInterviewList(interviewMapper.queryYesterdayInterviewByUserId(userId));
         scheduleInterviewDTO.setTodayInterviewList(interviewMapper.queryTodayInterviewByUserId(userId));
         scheduleInterviewDTO.setTomorrowInterviewList(interviewMapper.queryTomorrowInterviewByUserId(userId));
         return scheduleInterviewDTO;
     }

     public ScheduleInterviewDTO queryInterviewByCompanyId(Long companyId){
         ScheduleInterviewDTO scheduleInterviewDTO=new ScheduleInterviewDTO();
         scheduleInterviewDTO.setYesterdayInterviewList(interviewMapper.queryYesterdayInterviewByCompanyId(companyId));
         scheduleInterviewDTO.setTodayInterviewList(interviewMapper.queryTodayInterviewByCompanyId(companyId));
         scheduleInterviewDTO.setTomorrowInterviewList(interviewMapper.queryTomorrowInterviewByCompanyId(companyId));
         return scheduleInterviewDTO;
     }

    public List<Interview>queryInterviewByResume(Long resumeId){
         return interviewMapper.queryInterviewByResume(resumeId);
    }
}
