package com.resume.parse.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.parse.mapper.ResumeMapper;
import com.resume.parse.pojo.Resume;
import com.resume.parse.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-07-04
 */
@Service
public class ResumeService extends ServiceImpl<ResumeMapper, Resume> {

    @Autowired
    private RestTemplate restTemplate;

}
