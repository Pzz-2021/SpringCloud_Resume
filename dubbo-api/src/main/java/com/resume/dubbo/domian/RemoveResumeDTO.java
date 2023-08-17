package com.resume.dubbo.domian;

import lombok.Data;

import java.io.Serializable;

/*
 *@filename: RemoveResumeDTO
 *@author: lyh
 *@date:2023/8/17 16:42
 *@version 1.0
 *@description TODO
 */
@Data
public class RemoveResumeDTO implements Serializable {

    private Long targetPositionId;

    private String targetPositionName;

    private String preState;

    private Long prePositionId;

    private Long resumeId;

}
