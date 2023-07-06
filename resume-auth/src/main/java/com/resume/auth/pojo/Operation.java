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
    private Integer pkOperationId;

    /**
     * 请求方式(GET、POST、PUT、DELETE等)
     */
    private String method;

    /**
     * 操作编码
     */
    private String operationCode;

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
    private Integer isDeleted;


}
