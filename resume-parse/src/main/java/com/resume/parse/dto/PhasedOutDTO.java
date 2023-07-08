package com.resume.parse.dto;

import lombok.Data;

import java.io.Serializable;

/*
 *@filename: PhasedOutDTO
 *@author: lyh
 *@date:2023/7/8 15:58
 *@version 1.0
 *@description TODO
 */
@Data
public class PhasedOutDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long resumeId;

    //淘汰原因
    private String phasedOutCause;

}
