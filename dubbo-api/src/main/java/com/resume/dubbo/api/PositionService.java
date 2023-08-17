package com.resume.dubbo.api;

import com.resume.dubbo.domian.*;

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

    int addCandidateNum(Long positionId);

    void decreaseCandidateNum(RemoveResumeDTO removeResumeDTO);

    List<PositionTeam>queryOptionalInterviewer(Long positionId);

    Position getOne(Long positionId);

    HomeVo getHome(Long companyId);
}
