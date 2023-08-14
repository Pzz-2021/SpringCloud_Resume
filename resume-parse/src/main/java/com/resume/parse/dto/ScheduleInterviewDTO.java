package com.resume.parse.dto;

import com.resume.parse.pojo.Interview;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
 *@filename: ScheduleInterviewDTO
 *@author: lyh
 *@date:2023/8/14 15:29
 *@version 1.0
 *@description TODO
 */
@Data
public class ScheduleInterviewDTO implements Serializable {
    private List<Interview>yesterdayInterviewList;

    private List<Interview>todayInterviewList;

    private List<Interview>tomorrowInterviewList;
}
