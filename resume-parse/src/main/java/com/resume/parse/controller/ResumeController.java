package com.resume.parse.controller;

import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.DateUtil;
import com.resume.base.utils.JwtUtil;
import com.resume.parse.dto.PhasedOutDTO;
import com.resume.parse.pojo.Remark;
import com.resume.parse.service.RemarkService;
import com.resume.parse.service.ResumeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/*
 *@filename: ResumeController
 *@author: lyh
 *@date:2023/7/8 10:04
 *@version 1.0
 *@description TODO
 */
@RestController
@Api(tags = "简历接口")
@Slf4j
public class ResumeController {
    @Autowired
    private ResumeService resumeService;

    @Autowired
    private RemarkService remarkService;

    @ApiOperation(value = "修改简历状态",notes = "targetState指用户将简历移至的目标状态，可选项：初筛、面试、沟通Offer、待入职")
    @PutMapping("/change-resume-state")
    public RestResponse<String> changeResumeState(@RequestParam Long resumeId,@RequestParam String targetState) {
        boolean save = resumeService.changeResumeState(resumeId, targetState,null);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "淘汰简历")
    @PutMapping("/phased-out-resume")
    public RestResponse<String> phasedOutResume(@RequestBody PhasedOutDTO phasedOutDTO) {
        boolean save = resumeService.phasedOutResume(phasedOutDTO);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "备注简历",notes = "传resumeId、userName、userPicture、content")
    @PostMapping("/remark-resume")
    public RestResponse<String> remarkResume(HttpServletRequest httpServletRequest,@RequestBody Remark remark) {
        TokenInfo tokenInfo= JwtUtil.getTokenInfo(httpServletRequest);
        remark.setCompanyId(tokenInfo.getCompanyId());
        remark.setUserId(tokenInfo.getPkUserId());
        remark.setCreateTime(DateUtil.getDate2());
        boolean save = remarkService.save(remark);
        return RestResponse.judge(save);
    }


}
