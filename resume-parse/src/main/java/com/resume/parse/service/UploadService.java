package com.resume.parse.service;
import com.resume.base.utils.DateUtil;
import com.resume.dubbo.domian.Resume;
import com.resume.parse.utils.RedisConstants;
import com.resume.parse.utils.RedisUtil;
import com.resume.parse.utils.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pp
 * @since 2023-07-04
 */
@Service
@Slf4j
public class UploadService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UploadUtil uploadUtil;

    // 自定义连接池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);


    public boolean checkChunkExist(Long companyId, String identifier) {
        String key = RedisConstants.CACHE_ChECK_RESUME + companyId + ":" + identifier;
        // 返回为 是否存在  true-存在  false-不存在
        return redisUtil.hasKey(key);
    }

    // 添加成功返回 true  失败返回 false
    public void addChunk(Long companyId, String identifier, String url) {
        String key = RedisConstants.CACHE_ChECK_RESUME + companyId + ":" + identifier;
        redisUtil.set(key, url);
    }

    public void upload(MultipartFile file,Long companyId,String identifier) {
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        String newFilename = UUID.randomUUID().toString() + '-' + originalFilename;
        log.info("上传文件名：" + newFilename);
        //创建新的文件名称
        String fileURL = null;
        try {
            fileURL = uploadUtil.uploadByBytes(file.getBytes(), newFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //缓存
        addChunk(companyId, identifier, fileURL);
        //存入数据库
        Resume resume=new Resume();
        resume.setCompanyId(companyId);
        resume.setFileName(originalFilename);
        resume.setUrl(fileURL);
        resume.setCreateTime(DateUtil.getDate2());
        resume.setUpdateTime(DateUtil.getDate2());
        resume.setIdentifier(identifier);
        resumeService.save(resume);
        // 简历异步解析
        parseResume(resume.getPkResumeId(),resume.getUrl());
    }

    // 解析简历数据为结构化数据
    public void parseResume(Long pkResumeId, String url) {

        // 开启独立线程，实现解析
        CACHE_REBUILD_EXECUTOR.submit(() -> {
            try {
                // 调用Python程序解析
                resumeService.parseResume(pkResumeId, url);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
