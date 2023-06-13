package com.resume.base.utils;

/*
 *@filename: SM3Rule
 *@author: lyh
 *@date:2023/6/11 23:07
 *@version 1.0
 *@description TODO
 */

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 加密规则
 */
public enum SM3Rule {
    /**
     * 转码规则 与上面讲的对应
     */
    ZERO("0", "aa"),
    ONE("1", "bb"),
    TWO("2", "cc"),
    THREE("3", "dd"),
    FOUR("4", "ee"),
    FIVE("5", "ff"),
    SIX("6", "gg"),
    SEVEN("7", "hh"),
    EIGHT("8", "ii"),
    NINE("9", "jj")
    ;

    @Getter
    private String code;
    @Getter
    private String msg;

    SM3Rule(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String valueOfCode(String code) {
        for (SM3Rule sm3Rule : values()) {
            if (StringUtils.equals(code, sm3Rule.code)) {
                return sm3Rule.msg;
            }
        }
        throw new IllegalStateException("数字转换异常");
    }

}

