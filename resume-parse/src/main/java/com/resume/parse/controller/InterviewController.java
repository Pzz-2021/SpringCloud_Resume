package com.resume.parse.controller;
import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.Constant;
import com.resume.base.utils.JwtUtil;
import com.resume.parse.pojo.Interview;
import com.resume.parse.service.InterviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

    @ApiOperation(value = "查询所有的面试安排",notes = "不同角色查询的面试安排不同")
    @GetMapping("/query-all-interview")
    public RestResponse<List<Interview>> queryInterviewByRole(HttpServletRequest httpServletRequest) {
        TokenInfo tokenInfo= JwtUtil.getTokenInfo(httpServletRequest);
        String role=tokenInfo.getRole();
        if(Constant.COMPANY_ADMIN.equals(role)){
            List<Interview> interviews = interviewService.queryInterviewByCompanyId(tokenInfo.getCompanyId());
            return RestResponse.success(interviews);
        }
        else{
            List<Interview> interviews = interviewService.queryInterviewByUserId(tokenInfo.getPkUserId());
            return RestResponse.success(interviews);
        }
    }
    @ApiOperation(value = "查询单个简历的所有面试安排")
    @GetMapping("/query-resume-interview/{resumeId}")
    public RestResponse<List<Interview>> queryInterviewByResume(@PathVariable("resumeId")Long resumeId) {
          List<Interview>interviews=interviewService.queryInterviewByResume(resumeId);
          return RestResponse.success(interviews);
    }
    @ApiOperation(value = "查看可选的面试官")
    @PostMapping("/query-optional-interviewer")
    public RestResponse<Long> queryOptionalInterviewer(HttpServletRequest httpServletRequest) {
        TokenInfo tokenInfo= JwtUtil.getTokenInfo(httpServletRequest);
        
        return RestResponse.success();
    }
    @ApiOperation(value = "添加面试安排")
    @PostMapping("/add-interview")
    public RestResponse<Long> addInterview(HttpServletRequest httpServletRequest,@RequestBody Interview interview) {
        TokenInfo tokenInfo= JwtUtil.getTokenInfo(httpServletRequest);
        interview.setCompanyId(tokenInfo.getCompanyId());
        interviewService.save(interview);
        return RestResponse.success(interview.getPkInterviewId());
    }
}
