package com.resume.position.service;

import com.resume.dubbo.api.PositionService;
import com.resume.dubbo.domian.ResumeStateDTO;
import com.resume.position.mapper.PositionMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 *@filename: ResumeService
 *@author: lyh
 *@date:2023/7/8 21:43
 *@version 1.0
 *@description TODO
 */
@DubboService
public class ResumeService implements PositionService {
    @Autowired
    private PositionMapper positionMapper;

    private static final String OBSOLETE="已淘汰";

    @Override
    public int changePositionResumeCount(ResumeStateDTO resumeStateDTO) {
        return positionMapper.changePositionResumeCount(resumeStateDTO);
    }

    @Override
    public boolean addCandidateNum(Long positionId) {
        int low = positionMapper.addCandidateNum(positionId);
        return low > 0;
    }
}
