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

    public static final String EXPIRED_JWT_EXCEPTION="Token过期";

    public static final String UNSUPPORTED_JWT_EXCEPTION="不支持的Token";

    public static final String MALFORMED_JWT_EXCEPTION="Token格式错误";

    public static final String SIGNATURE_EXCEPTION="Token签名异常";

    public static final String ILLEGAL_ARGUMENT_EXCEPTION="非法请求";

    public static final String EXCEPTION="Token解析异常";

    public static final String RIGHT_TOKEN="Token正确";

    public static final String INSUFFICIENT_PERMISSIONS="权限不足";
    /**
    redis
    */

    public static final String USER_KEY="resume:user:";
    public static final Long TOKEN_TTL=24L;
}
