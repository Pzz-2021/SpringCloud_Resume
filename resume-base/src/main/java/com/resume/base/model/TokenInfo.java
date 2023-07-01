package com.resume.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 *@filename: TokenInfo
 *@author: lyh
 *@date:2023/7/1 16:05
 *@version 1.0
 *@description TODO
 */
@AllArgsConstructor
@Data
public class TokenInfo {

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
