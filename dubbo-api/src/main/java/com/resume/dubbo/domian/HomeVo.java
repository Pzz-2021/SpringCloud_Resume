package com.resume.dubbo.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeVo implements Serializable {
    /*
        职位
     */
    // 正在招聘的职位数
    private int positioningCount;
    // 待入职
    private int preEmployCount;
    // 已入职
    private int afterEmployCount;
    // 目标招聘
    private int targetCount;

    /*
        简历
     */
    // 新简历数
    private int newResumeCount;
    // 本周面试数
    private int interviewCount;
}
