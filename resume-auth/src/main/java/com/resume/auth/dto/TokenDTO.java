package com.resume.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/*
 *@filename: Token
 *@author: lyh
 *@date:2023/6/18 0:52
 *@version 1.0
 *@description TODO
 */
@Data
public class TokenDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "access_token")
    private String access_token;
    @ApiModelProperty(value = "refresh_token")
    private String refresh_token;
}
