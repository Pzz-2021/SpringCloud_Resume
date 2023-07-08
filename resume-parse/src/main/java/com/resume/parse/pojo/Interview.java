package com.resume.parse.pojo;

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
 * @since 2023-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Interview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 面试标识ID
     */
    @TableId(value = "pk_interview_id", type = IdType.AUTO)
    private Long pkInterviewId;

    /**
     * 面试的简历ID（我们的系统不面向求职者）
     */
    private Long resumeId;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 面试官ID（userId）
     */
    private Long interviewerId;

    /**
     * 面试开始时间
     */
    private LocalDateTime startDatetime;

    /**
     * 持续的时间-该数值以半小时为单位
     */
    private Integer durationTime;

    /**
     * 面试类型（电话面试、现场面试、视频面试）
     */
    private String interviewType;

    /**
     * 面试地点
     */
    private String interviewLocation;

    /**
     * 面试进度（初试、复试、终试）
     */
    private String interviewProgress;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 删除：1-已删除、0-未删除
     */
    @TableLogic
    private Integer isDeleted;


}
