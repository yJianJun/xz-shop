package com.cdzg.xzshop.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分页参数模型")
public class PageParam {

    @ApiModelProperty(value = "当前页码", position = 1, required = true)
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页面数据条数", position = 2, required = true)
    private Integer pageSize = 10;

}
