package com.cdzg.xzshop.to.app;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName : TrainingModuleRespVO
 * @Description : 培训模块信息(app培训模块首页)
 * @Author : XLQ
 * @Date: 2021-01-15 14:10
 */

@Data
@ApiModel("培训模块信息")
public class TrainingModuleRespTo implements Serializable {
    private static final long serialVersionUID = 1682232629166484487L;


    @ApiModelProperty(value = "模块id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long moduleId;

    @ApiModelProperty(value = "模块Name")
    private String moduleName;

    @ApiModelProperty(value = "课程信息列表")
    private List<TrainingCurriculumRespTo> curriculumList;
}
