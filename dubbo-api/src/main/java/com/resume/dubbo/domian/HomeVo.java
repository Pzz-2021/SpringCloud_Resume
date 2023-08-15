package com.resume.dubbo.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeVo implements Serializable {
    private int positioningCount;
    private int preEmployCount;
    private int afterEmployCount;
    private int targetCount;

    private int newResumeCount;
    private int interviewCount;
}
