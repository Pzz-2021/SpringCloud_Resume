package com.resume.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 *@filename: TokenInfo
 *@author: lyh
 *@date:2023/7/1 16:05
 *@version 1.0
 *@description TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo implements Serializable {

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
     * 用户角色
     */
    private String role;
}
