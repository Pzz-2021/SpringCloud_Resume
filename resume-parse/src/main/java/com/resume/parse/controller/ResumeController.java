package com.resume.parse.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.resume.base.model.PageBean;
import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.DateUtil;
import com.resume.base.utils.JwtUtil;
import com.resume.dubbo.domian.Resume;
import com.resume.dubbo.domian.ResumeStateDTO;
import com.resume.dubbo.domian.SearchCondition;
import com.resume.parse.dto.SchoolDTO;
import com.resume.parse.pojo.Remark;
import com.resume.parse.service.RemarkService;
import com.resume.parse.service.ResumeService;
import com.resume.parse.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "移动简历", notes = "前端传要移动到的目标职位id、职位名、以及简历id")
    @PostMapping("/removeResume")
    public RestResponse<String> removeResume(@RequestBody ResumeStateDTO resumeStateDTO) {
        boolean save=resumeService.removeResume(resumeStateDTO);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "修改简历状态", notes = "targetState指用户将简历移至的目标状态，可选项：初筛、面试、沟通Offer、待入职")
    @PutMapping("/change-resume-state")
    public RestResponse<String> changeResumeState(@RequestBody ResumeStateDTO resumeStateDTO) {
        boolean save = resumeService.changeResumeState(resumeStateDTO);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "淘汰简历")
    @PutMapping("/phased-out-resume")
    public RestResponse<String> phasedOutResume(@RequestBody ResumeStateDTO resumeStateDTO) {
        boolean save = resumeService.phasedOutResume(resumeStateDTO);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "备注简历", notes = "传resumeId、userName、userPicture、content")
    @PostMapping("/remark-resume")
    public RestResponse<String> remarkResume(HttpServletRequest httpServletRequest, @RequestBody Remark remark) {
        TokenInfo tokenInfo = JwtUtil.getTokenInfo(httpServletRequest);
        remark.setCompanyId(tokenInfo.getCompanyId());
        remark.setUserId(tokenInfo.getPkUserId());
        remark.setCreateTime(DateUtil.getDate2());
        boolean save = remarkService.save(remark);
        return RestResponse.judge(save);
    }

    @ApiOperation(value = "查询一个简历")
    @GetMapping("/get-one-resume")
    public RestResponse<Resume> getOne(Long pkResumeId) {
        Resume resume = resumeService.getOneByEs(pkResumeId);
        resumeToVo(resume);
        return RestResponse.judge(resume);
    }

    @ApiOperation(value = "分页查询简历")
    @PostMapping("/select-resume/by-page")
    public RestResponse<PageBean<Resume>> selectResumeByEs(HttpServletRequest httpServletRequest, @RequestBody SearchCondition searchCondition) {
        TokenInfo tokenInfo = JwtUtil.getTokenInfo(httpServletRequest);
        PageBean<Resume> resumePageBean = resumeService.selectResumeByEs(searchCondition, tokenInfo);
        resumeToVo(resumePageBean.getData().toArray(new Resume[0]));

        return RestResponse.success(resumePageBean);
    }

    @ApiOperation(value = "分页查询简历（测试）")
    @PostMapping("/select-resume/by-page-test")
    public RestResponse<PageBean<Resume>> selectResumeByEsTest(@RequestBody SearchCondition searchCondition) {
        TokenInfo tokenInfo = new TokenInfo(1L, 1L, "超级管理员");
        PageBean<Resume> resumePageBean = resumeService.selectResumeByEs(searchCondition, tokenInfo);
        resumeToVo(resumePageBean.getData().toArray(new Resume[0]));

        return RestResponse.success(resumePageBean);
    }


    private void resumeToVo(Resume... resumes) {
        String PATH = "school:info:";

        for (Resume resume : resumes) {
            String content = resume.getResumeContent();
            if(content==null)continue;
            if (content.contains("女"))
                resume.setImg("https://ats.xiaoxizn.com/assets/17823c8dba3a87a03ff8ac2197dab9c5.svg");
            else
                resume.setImg("https://ats.xiaoxizn.com/assets/4c1dfa88596748bf19af303827ab0fcc.svg");

            SchoolDTO schoolDTO = (SchoolDTO) redisUtil.get(PATH + resume.getGraduateInstitution());
            if (schoolDTO != null) {
                if (schoolDTO.getIsHigher())
                    resume.getTags_good().add("国内" + String.join("/", schoolDTO.getUnivTags()));
                if (schoolDTO.getRank() <= 10)
                    resume.getTags_good().add("国内TOP10");
            }
            if (content.contains("管理"))
                resume.getTags_good().add("管理经验");
            if (content.contains("英语"))
                resume.getTags_good().add("英语能力良好");
            if (content.contains("腾讯") || content.contains("网易") || content.contains("字节") || content.contains("阿里"))
                resume.getTags_good().add("名企经历");

            JSONObject jsonObject = JSONObject.parseObject(resume.getJsonContent());
            int count = jsonObject.getJSONArray("工作经历").size();
            if (resume.getWorkingYears() / count >= 2)
                resume.getTags_good().add("稳定性高");
            else if (resume.getWorkingYears() / count == 0)
                resume.getTags_bad().add("稳定性低");

            JSONArray graduates = jsonObject.getJSONArray("教育背景");
            List<String> list = new ArrayList<>();
            if (graduates == null)
                continue;
            for (int i = 0; i < graduates.size(); i++) {
                Object o = graduates.get(i);
                if (o == null)
                    continue;

                JSONObject object = (JSONObject) (o);
                String graduate = (String) object.get("学位");

                if (graduate == null)
                    continue;
                if (graduate.equals("大专")) {
                    resume.getTags_bad().add("专科");

                    if (list.contains("本科"))
                        resume.getTags_bad().add("专升本");
                } else if (graduate.equals("本科")) {
                    list.add("本科");

                    if (resume.getTags_bad().contains("专科"))
                        resume.getTags_bad().add("专升本");
                }
            }
        }
    }
}
