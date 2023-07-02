package com.resume.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T>{
     private String searchWord;
     private int totalCount;//总记录数
     private int totalPage;//总页数
     private int nowPage;//当前页码
     private List<T> data;//每页显示的数据集合
}
