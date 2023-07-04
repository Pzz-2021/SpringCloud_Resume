package com.resume.parse.dto;

import lombok.Data;

import java.io.Serializable;

/*
 *@filename: FileChunkDTO
 *@author: lyh
 *@date:2023/7/4 20:53
 *@version 1.0
 *@description TODO
 */
@Data
public class FileChunkDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 文件md5
     */
    private String identifier;
}
