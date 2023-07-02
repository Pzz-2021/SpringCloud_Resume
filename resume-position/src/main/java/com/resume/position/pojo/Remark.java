package com.resume.position.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 彭政
 * @since 2023-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Remark implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 备注ID
     */
    @TableId(value = "pk_remark_id", type = IdType.AUTO)
    private Long pkRemarkId;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 公司成员ID：备注人
     */
    private Long userId;

    /**
     * 简历ID
     */
    private Long resumeId;

    /**
     * 备注人昵称
     */
    private String userName;
    /**
     * 备注人头像
     */
    private String userPicture;

    /**
     * 备注内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 删除状态：1-已删除、0-未删除
     */
    @TableLogic
    private Integer isDeleted;


}
