package com.resume.auth.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作ID
     */
    @TableId(value = "pk_operation_id", type = IdType.AUTO)
    private Long pkOperationId;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 编码规则，对象:操作名
     */
    private String operationCode;

    /**
     * 操作名(添加、删除、查询等)
     */
    private String method;

    /**
     * 接口url
     */
    private String interfaceUrl;

    /**
     * 描述
     */
    private String description;

    /**
     * 删除状态：1-已删除、0-未删除
     */
    @TableLogic
    private Boolean isDeleted;


}
