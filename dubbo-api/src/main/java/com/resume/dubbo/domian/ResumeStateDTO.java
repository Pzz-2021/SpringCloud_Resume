package com.resume.dubbo.domian;

import lombok.Data;

import java.io.Serializable;

/*
 *@filename: ResumeStateDTO
 *@author: lyh
 *@date:2023/7/8 20:45
 *@version 1.0
 *@description TODO
 */

@Data
public class ResumeStateDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long positionId;

    private Long resumeId;
    //先前的状态
    private String preState;

    //目标状态
    private String targetState;

    //淘汰原因
    private String phasedOutCause;

}
