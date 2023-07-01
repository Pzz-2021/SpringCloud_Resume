package com.resume.base.model;

import lombok.Data;

import java.util.List;

@Data
public class PageBean<T>{
     private String searchWord;
     private int totalCount;//总记录数
     private int totalPage;//总页数
     private int nowPage;//当前页码
     private List<T> data;//每页显示的数据集合
}
