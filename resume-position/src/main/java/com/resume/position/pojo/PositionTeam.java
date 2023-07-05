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
 * @author lyh
 * @since 2023-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PositionTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "pk_id", type = IdType.AUTO)
    private Long pkId;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 3为HR，4为面试官
     */
    private Integer roleId;

    /**
     * 角色名
     */
    private String roleName;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userPicture;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 删除状态：1-已删除、0-未删除
     */
    @TableLogic
    private Integer isDeleted;


}
