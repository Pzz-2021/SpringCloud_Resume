package com.resume.parse.controller;
import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.Constant;
import com.resume.base.utils.JwtUtil;
import com.resume.dubbo.api.PositionService;
import com.resume.dubbo.domian.PositionTeam;
import com.resume.parse.dto.ScheduleInterviewDTO;
import com.resume.parse.pojo.Interview;
import com.resume.parse.service.InterviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
 *@filename: InterviewController
 *@author: lyh
 *@date:2023/7/10 23:00
 *@version 1.0
 *@description TODO
 */
@RestController
@Api(tags = "面试接口")
@Slf4j
public class InterviewController {
    @Autowired
    private InterviewService interviewService;

    @DubboReference
    private PositionService positionService;

    @ApiOperation(value = "查询所有的面试安排",notes = "不同角色查询的面试安排不同，分为昨天、今天、明天")
    @GetMapping("/query-all-interview")
    public RestResponse<ScheduleInterviewDTO> queryInterviewByRole(HttpServletRequest httpServletRequest) {
        TokenInfo tokenInfo= JwtUtil.getTokenInfo(httpServletRequest);
        String role=tokenInfo.getRole();
        if(Constant.COMPANY_ADMIN.equals(role)){
            ScheduleInterviewDTO scheduleInterviewDTO = interviewService.queryInterviewByCompanyId(tokenInfo.getCompanyId());
            return RestResponse.success(scheduleInterviewDTO);
        }
        else{
            ScheduleInterviewDTO scheduleInterviewDTO = interviewService.queryInterviewByUserId(tokenInfo.getPkUserId());
            return RestResponse.success(scheduleInterviewDTO);
        }
    }
    @ApiOperation(value = "查询单个简历的所有面试安排")
    @GetMapping("/query-resume-interview/{resumeId}")
    public RestResponse<List<Interview>> queryInterviewByResume(@PathVariable("resumeId")Long resumeId) {
          List<Interview>interviews=interviewService.queryInterviewByResume(resumeId);
          return RestResponse.success(interviews);
    }
    @ApiOperation(value = "查看可选的面试官")
    @GetMapping("/query-optional-interviewer/{positionId}")
    public RestResponse<List<PositionTeam>> queryOptionalInterviewer(@PathVariable("positionId")Long positionId) {
        //position_id, user_id, role_id, role_name, user_name, user_picture
        List<PositionTeam> positionTeams = positionService.queryOptionalInterviewer(positionId);
        return RestResponse.success(positionTeams);
    }
    @ApiOperation(value = "添加面试安排",notes = "resumeId简历id、resumeUserName候选人名、interviewerId面试官id、" +
            "interviewerName面试官名字、positionId面试的职位id、positionName面试的职位名称、startDate面试日期、startTime面试具体时间、" +
            "durationTime持续的时间-该数值以半小时为单位、interviewType面试类型（电话面试、现场面试、视频面试）、interviewLocation面试地点")
    @PostMapping("/add-interview")
    public RestResponse<Long> addInterview(HttpServletRequest httpServletRequest,@RequestBody Interview interview) {
        TokenInfo tokenInfo= JwtUtil.getTokenInfo(httpServletRequest);
        interview.setCompanyId(tokenInfo.getCompanyId());
        interviewService.save(interview);
        return RestResponse.success(interview.getPkInterviewId());
    }
}
