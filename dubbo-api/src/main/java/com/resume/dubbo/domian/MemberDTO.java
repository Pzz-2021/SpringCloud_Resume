package com.resume.dubbo.domian;

import lombok.Data;

import java.io.Serializable;

/*
 *@filename: MemberDTO
 *@author: lyh
 *@date:2023/7/2 19:31
 *@version 1.0
 *@description TODO
 */
@Data
public class MemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private String pkUserId;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户头像
     */
    private String accountPicture;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户角色
     */
    private String roleName;
}
