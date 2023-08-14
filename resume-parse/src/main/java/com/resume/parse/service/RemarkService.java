package com.resume.parse.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.resume.parse.mapper.RemarkMapper;
import com.resume.parse.pojo.Remark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyh
 * @since 2023-07-04
 */
@Service
public class RemarkService extends ServiceImpl<RemarkMapper, Remark> {
    @Resource
    private RemarkMapper remarkMapper;

    public RemarkMapper getRemarkMapper() {
        return remarkMapper;
    }
}
