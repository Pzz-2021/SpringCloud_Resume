package com.resume.parse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDTO implements Serializable {
    private int rank;
    private String univName;
    private float score;
    private String province;
    private String category;
    private Boolean isHigher;
    private List<String> univTags;
}
