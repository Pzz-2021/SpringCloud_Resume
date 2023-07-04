package com.resume.parse.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyh
 * @since 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @TableId(value = "pk_file_id", type = IdType.AUTO)
    private Long pkFileId;

    /**
     * 文件真实名字
     */
    private String realName;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 检验码 md5
     */
    private String identifier;

    /**
     * 发布人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
