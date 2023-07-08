package com.resume.parse.controller;

import com.resume.base.model.RestResponse;
import com.resume.base.model.TokenInfo;
import com.resume.base.utils.JwtUtil;
import com.resume.parse.dto.FileChunkDTO;
import com.resume.parse.service.UploadService;
import com.resume.parse.utils.RedisConstants;
import com.resume.parse.utils.RedisUtil;
import com.resume.parse.utils.UploadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

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
    private UploadUtil uploadUtil;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "单个简历上传")
    @PostMapping("/upload-single-resume")
    public RestResponse<String> uploadSingleResume(HttpServletRequest httpServletRequest,@RequestParam("file")MultipartFile file,@RequestParam String identifier) throws IOException {
        TokenInfo tokenInfo = JwtUtil.getTokenInfo(httpServletRequest);
        //缓存里不存在
        if(!uploadService.checkChunkExist(tokenInfo.getCompanyId(),identifier)) {
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();
            String newFilename = UUID.randomUUID().toString() + '-' + originalFilename;
            log.info("上传文件名：" + newFilename);
            //创建新的文件名称
            String fileURL = uploadUtil.uploadByBytes(file.getBytes(), newFilename);
            uploadService.addChunk(tokenInfo.getCompanyId(), identifier,fileURL);
            return RestResponse.success(fileURL);
        }
        //缓存命中
        else{
            String key = RedisConstants.CACHE_ChECK_RESUME + tokenInfo.getCompanyId()+identifier;
            return RestResponse.success((String) redisUtil.get(key));
        }
    }
}
