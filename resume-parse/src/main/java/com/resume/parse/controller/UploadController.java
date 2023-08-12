package com.resume.parse.controller;

import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.JwtUtil;
import com.resume.parse.service.ResumeService;
import com.resume.parse.service.UploadService;
import com.resume.parse.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/*
 *@filename: SingleUploadController
 *@author: lyh
 *@date:2023/7/4 16:33
 *@version 1.0
 *@description TODO
 */
@RestController
@Api(tags = "简历上传接口")
@Slf4j
public class UploadController {

    @Autowired
    private UploadService uploadService;


    @ApiOperation(value = "单个简历上传")
    @PostMapping("/upload-single-resume")
    public RestResponse<String> uploadSingleResume(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file, @RequestParam String identifier) {
        TokenInfo tokenInfo = JwtUtil.getTokenInfo(httpServletRequest);
        //缓存里不存在
        if (!uploadService.checkChunkExist(tokenInfo.getCompanyId(), identifier)) {
            uploadService.upload(file, tokenInfo.getCompanyId(), identifier);
            return RestResponse.success("上传成功");
        }
        //缓存命中
        else {
            return RestResponse.error("上传失败");
        }
    }
}
