package com.resume.parse.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.parse.dto.FileChunkDTO;
import com.resume.parse.mapper.ResumeMapper;
import com.resume.parse.pojo.Resume;
import com.resume.parse.utils.RedisConstants;
import com.resume.parse.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class UploadService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ResumeService resumeService;

    //自定义连接池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);


    public boolean checkChunkExist(Long companyId, String identifier) {
        String key = RedisConstants.CACHE_ChECK_RESUME + companyId + ":" + identifier;
        // 返回为 是否存在  true-存在  false-不存在
        return redisUtil.hasKey(key);
    }

    // 添加成功返回 true  失败返回 false
    public boolean addChunk(Long companyId, String identifier, String url) {
        String key = RedisConstants.CACHE_ChECK_RESUME + companyId + ":" + identifier;
        return redisUtil.set(key, url);
    }


    // 解析简历数据为结构化数据
    public void parseResume(Long pkResumeId, String url) {

        // 6.3.成功，开启独立线程，实现解析
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
