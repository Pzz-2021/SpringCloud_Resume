package com.resume.parse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.resume.parse.pojo.Remark;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyh
 * @since 2023-07-04
 */
public interface RemarkMapper extends BaseMapper<Remark> {
    List<Remark>getResumeRemark(Long resumeId);
}
