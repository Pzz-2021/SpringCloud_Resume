package com.resume.auth.dto;

import com.resume.auth.pojo.Menu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/*
 *@filename: LoginDTO
 *@author: lyh
 *@date:2023/6/11 21:17
 *@version 1.0
 *@description TODO
 */
@Data
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户信息")
    private UserInfoDTO userInfoDTO;

    @ApiModelProperty(value = "access_token")
    private String access_token;

    @ApiModelProperty(value = "refresh_token")
    private String refresh_token;

    //给前端的
    @ApiModelProperty(value = "权限列表")
    private Map<String, Boolean> permissionsList;

    @ApiModelProperty(value = "菜单列表")
    private List<Menu>menusList;
}
