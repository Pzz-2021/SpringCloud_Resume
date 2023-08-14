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
     * 角色
     */
    public static final String SUPER_ADMIN = "超级管理员";
    public static final String COMPANY_ADMIN = "公司管理员";
    public static final String HR = "HR";
    public static final String INTERVIEWER = "面试官";
    /**
     * 认证、鉴权
     */
    public static final String UNAUTHORIZED_EXCEPTION = "用户未认证";
    public static final String EXCEPTION = "Token解析异常";
    public static final int UNAUTHORIZED_EXCEPTION_CODE = 401;

    public static final String EXPIRED_JWT_EXCEPTION = "Token过期";
    public static final int EXPIRED_JWT_EXCEPTION_CODE = 555;

    public static final String RIGHT_TOKEN = "Token正确";

    public static final String INSUFFICIENT_PERMISSIONS = "权限不足";
    public static final int INSUFFICIENT_PERMISSIONS_CODE = 403;

    /**
     * 分页条数
     */
    public static final int PAGE_SIZE = 10;

    /**
     * redis
     */

    public static final String USER_KEY = "resume:user:";
    public static final Long USER_TTL = 24L * 60 * 60;

    /**
     * 简历状态
     */
    public static final String FIRST_SCREENER="初筛";
    public static final String INTERVIEW="面试";
    public static final String COMMUNICATE_OFFER="沟通Offer";
    public static final String PEND_EMPLOY="待入职";
    public static final String EMPLOYED_EMPLOY="已入职";
    public static final String OBSOLETE="已淘汰";
    public static final String UNCHECKED="未选中";

}
