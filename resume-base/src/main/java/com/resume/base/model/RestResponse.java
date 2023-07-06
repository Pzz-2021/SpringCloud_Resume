package com.resume.base.model;

import lombok.Data;

import java.io.Serializable;

/*
 *@filename: RestResponse
 *@author: lyh
 *@date:2023/6/1 21:49
 *@version 1.0
 *@description TODO
 */
@Data
public class RestResponse<T> implements Serializable {
    private Integer code; //编码：1成功，0和其它数字为失败
    private String message; //错误信息
    private T data; //数据

    //把构造方法私有
    private RestResponse() {
    }

    public static <T> RestResponse<T> success() {
        RestResponse<T> response = new RestResponse<T>();
        response.code = 1;
        return response;
    }

    public static <T> RestResponse<T> success(T object) {
        RestResponse<T> response = success();
        response.code = 1;
        response.data = object;
        return response;
    }

    public static <T> RestResponse<T> error() {
        RestResponse<T> response = new RestResponse<T>();
        response.code = 0;
        response.message = "请求失败";
        return response;
    }

    public static <T> RestResponse<T> error(T object) {
        RestResponse<T> response = error();
        response.code = 0;
        response.data = object;
        return response;
    }

    public static <T> RestResponse<T> error(String message) {
        RestResponse<T> response = error();
        response.code = 0;
        response.message = message;
        return response;
    }

    public static <T> RestResponse<T> error(String message, T object) {
        RestResponse<T> response = error();
        response.code = 0;
        response.message = message;
        response.data = object;
        return response;
    }

    public static RestResponse<String> judge(boolean save) {
        return save ? success() : error();
    }

    public static <T> RestResponse<T> judge(T object) {
        return object != null ? success(object) : error();
    }
}
