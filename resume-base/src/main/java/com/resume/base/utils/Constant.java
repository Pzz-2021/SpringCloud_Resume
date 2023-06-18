package com.resume.base.utils;

/*
 *@filename: ErrorConstant
 *@author: lyh
 *@date:2023/6/13 8:38
 *@version 1.0
 *@description TODO
 */
public class Constant {
    /**
    认证、鉴权
    */
    public static final String UNAUTHORIZED_EXCEPTION="用户未认证";
    public static final String EXCEPTION="Token解析异常";
    public static final int UNAUTHORIZED_EXCEPTION_CODE=401;

    public static final String EXPIRED_JWT_EXCEPTION="Token过期";
    public static final int EXPIRED_JWT_EXCEPTION_CODE=555;

    public static final String RIGHT_TOKEN="Token正确";

    public static final String INSUFFICIENT_PERMISSIONS="权限不足";
    public static final int INSUFFICIENT_PERMISSIONS_CODE=403;

    /**
    redis
    */

    public static final String USER_KEY="resume:user:";
    public static final Long USER_TTL=24L;

    public static final String VERIFY_CODE_KEY="resume:verifyCode:";
    public static final Long VERIFY_CODE_TTL=10L;
}
