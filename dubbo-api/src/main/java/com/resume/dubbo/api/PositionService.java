package com.resume.dubbo.api;

import com.resume.dubbo.domian.Position;
import com.resume.dubbo.domian.PositionTeam;
import com.resume.dubbo.domian.ResumeStateDTO;

import java.util.List;

/*
 *@filename: PositionService
 *@author: lyh
 *@date:2023/7/8 21:35
 *@version 1.0
 *@description TODO
 */
public interface PositionService {
    int changePositionResumeCount(ResumeStateDTO resumeStateDTO);

    boolean addCandidateNum(Long positionId);

    List<PositionTeam>queryOptionalInterviewer(Long positionId);

    Position getOne(Long positionId);
}
