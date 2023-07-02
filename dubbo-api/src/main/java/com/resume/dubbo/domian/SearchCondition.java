package com.resume.dubbo.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCondition implements Serializable {
    private int state;

    private String query;

    private int page;
    private int pageSize;
}
