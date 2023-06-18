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
public class PositionHr implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "pk_id", type = IdType.ASSIGN_ID)
    private Long pkId;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * HR的ID(userId)
     */
    private Long hrId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除状态：1-已删除、0-未删除
     */
    @TableLogic
    private Integer isDeleted;


}
