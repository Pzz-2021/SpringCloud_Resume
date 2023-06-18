package com.resume.auth.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/*
 *@filename: UserDTO
 *@author: lyh
 *@date:2023/6/11 21:29
 *@version 1.0
 *@description TODO
 */
@Data
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long pkUserId;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 用户头像
     */
    private String accountPicture;

    /**
     * 用户邮箱
     */
    private String userEmail;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户签名
     */
    private String personalSignature;
}
