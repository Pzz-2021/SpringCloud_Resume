package com.resume.dubbo.api;

import com.resume.dubbo.domian.ResumeStateDTO;

/*
 *@filename: PositionService
 *@author: lyh
 *@date:2023/7/8 21:35
 *@version 1.0
 *@description TODO
 */
public interface PositionService {
    int changePositionResumeCount(ResumeStateDTO resumeStateDTO);
}
