package com.resume.auth.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyh
 * @since 2023-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(value = "pk_role_id", type = IdType.AUTO)
    private Integer pkRoleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 1表示启用，0表示未启用
     */
    private Boolean isEnable;


}
