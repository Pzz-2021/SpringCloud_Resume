package com.resume.gateway.response;

import java.io.Serializable;

/**
 * 错误响应参数包装
 */
public class RestErrorResponse implements Serializable {
    private int errorCode;
    private String errMessage;

    public RestErrorResponse(int errorCode, String errMessage) {
        this.errorCode = errorCode;
        this.errMessage = errMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
