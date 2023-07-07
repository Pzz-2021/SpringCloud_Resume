package com.resume.auth.pojo;

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
 * @author lyh
 * @since 2023-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    @TableId(value = "pk_company_id", type = IdType.AUTO)
    private Long pkCompanyId;

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 公司logo
     */
    private String companyLogo;

    /**
     * 公司介绍
     */
    private String companyIntroduce;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更改用户ID
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 审核状态：2-通过、1-审核中、0-未通过
     */
    private Integer state;

    /**
     * 删除状态：1-已删除、0-未删除
     */
    @TableLogic
    private Integer isDeleted;
}
