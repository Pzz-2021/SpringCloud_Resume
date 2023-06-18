package com.resume.auth.dto;

import lombok.Data;

import java.io.Serializable;

/*
 *@filename: EnrollDTO
 *@author: lyh
 *@date:2023/6/18 1:23
 *@version 1.0
 *@description TODO
 */
@Data
public class EnrollDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;


    /**
     * 公司名
     */
    private String companyName;

    /**
     *  验证码
     */
    private String code;

}
