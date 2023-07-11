package com.resume.dubbo.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCondition implements Serializable {
    // 搜索的值
    private String query;

    // 状态 1 在招  0 未招  -1不限
    private Integer state;

    // 分页
    private Integer page;
    private Integer pageSize;


}
