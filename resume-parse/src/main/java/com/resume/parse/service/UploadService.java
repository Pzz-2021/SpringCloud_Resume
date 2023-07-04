package com.resume.parse.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.parse.dto.FileChunkDTO;
import com.resume.parse.mapper.ResumeMapper;
import com.resume.parse.pojo.Resume;
import com.resume.parse.utils.RedisConstants;
import com.resume.parse.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pp
 * @since 2023-07-04
 */
@Service
public class UploadService {
    @Autowired
    private RedisUtil redisUtil;

    public boolean checkChunkExist(Long companyId, String identifier) {
        String key = RedisConstants.CACHE_ChECK_RESUME + companyId;
        // 返回为 是否存在  true-存在  false-不存在
        return redisUtil.sHasKey(key, identifier);
    }

    // 添加成功返回 true  失败返回 false
    public boolean addChunk(Long companyId, String identifier) {
        String key = RedisConstants.CACHE_ChECK_RESUME + companyId;
        long count = redisUtil.sSet(key, identifier);
        return count > 0;
    }
}
