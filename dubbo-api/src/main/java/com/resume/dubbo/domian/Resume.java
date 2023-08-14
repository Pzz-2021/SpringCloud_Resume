package com.resume.dubbo.domian;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 简历ID
     */
    @TableId(value = "pk_resume_id", type = IdType.AUTO)
    private Long pkResumeId;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 简历的岗位ID
     */
    private Long positionId;

    /**
     * 简历的岗位名称
     */
    private String positionName;

    /**
     * 简历投递阶段：初筛、面试、沟通Offer、待入职、已淘汰、未投递
     */
    private String state;

    /**
     * 淘汰原因
     */
    private String phasedOutCause;

    /**
     * 简历文件名称
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String url;

    /**
     * pdf简历文件路径
     */
    private String pdfUrl;

    /**
     * 简历检验码 md5
     */
    private String identifier;

    /**
     * 简历文字内容
     */
    private String resumeContent;

    /**
     * 简历中JSON内容
     */
    private String jsonContent;

    /**
     * 简历中姓名
     */
    private String name;

    /**
     * 简历中年龄信息
     */
    private Integer age;

    /**
     * 简历中最高学历
     */
    private String educationBackground;

    /**
     * 简历中毕业院校
     */
    private String graduateInstitution;

    /**
     * 简历中工作年限
     */
    private Integer workingYears;

    /**
     * 解析状态：1-解析完成、0-未解析
     */
    private Integer isParsed;

    /**
     * 删除状态：1-已删除、0-未删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;


    @TableField(exist = false)
    private String img;

    @TableField(exist = false)
    private double score;

    @TableField(exist = false)
    private List<String> tags_good = new ArrayList<>();

    @TableField(exist = false)
    private List<String> tags_bad = new ArrayList<>();

    @TableField(exist = false)
    private double[] abilitys = new double[5];

    @TableField(exist = false)
    private double[] industryBackgrounds = new double[7];
}
