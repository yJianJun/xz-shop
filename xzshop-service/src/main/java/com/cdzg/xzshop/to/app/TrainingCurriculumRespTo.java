package com.cdzg.xzshop.to.app;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName : TrainingCurriculumRespVO
 * @Description : 课程基础信息
 * @Author : XLQ
 * @Date: 2021-01-18 09:35
 */

@Data
@ApiModel("课程基础信息")
public class TrainingCurriculumRespTo implements Serializable {

    private static final long serialVersionUID = 5447055676705922571L;

    /**
     * 课程id
     */
    @ApiModelProperty(value = "课程id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 课程名称
     */
    @ApiModelProperty(value = "课程名称")
    private String curriculumName;

    /**
     * 课程类型 1-文章课程  2-微视频课程
     */
    @ApiModelProperty(value = "课程类型 1-文章课程  2-微视频课程")
    private Integer curriculumType;

    /**
     * 视频url
     */
    @ApiModelProperty(value = "视频url")
    private String videoUrl;

    /**
     * 封面类型 0-无 1-大图 2-三图 3-单图
     */
    @ApiModelProperty(value = "封面类型 0-无 1-大图 2-三图 3-单图")
    private Integer coverType;

    /**
     * 封面图片url,多图使用,隔开
     */
    @ApiModelProperty(value = "封面图片url,多图使用,隔开")
    private String coverImage;



}
