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
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId(value = "pk_menu_id", type = IdType.AUTO)
    private Long pkMenuId;

    /**
     * 菜单名
     */
    private String menuName;

    /**
     * 路由的URL
     */
    private String url;

    /**
     * 图标
     */
    private String icon;

    /**
     * 功能描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer sortValue;

    /**
     * 状态：1-启用、0-未启用
     */
    private Boolean isEnable;


}
