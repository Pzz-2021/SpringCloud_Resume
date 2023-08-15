package com.resume.position.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
 *@filename: InterviewTrendDTO
 *@author: lyh
 *@date:2023/8/15 19:29
 *@version 1.0
 *@description TODO
 */
@Data
public class InterviewTrendDTO implements Serializable {

    private List<String>date;

    private List<Integer>newInterviewNumber;
}
