package com.cdzg.xzshop.vo.admin;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "商品分类添加参数模型")
public class GoodsCategoryAddVo {

    @ApiModelProperty(value = "分类等级", position = 1,required = true,allowableValues = "1,2")
    @NotNull
    private Integer level;

    @ApiModelProperty(value = "上1级分类id,如果等级是1级分类，可不传", position = 2,allowEmptyValue = true)
    private Long parentId;

    @ApiModelProperty(value = "分类名称", position = 3,required = true)
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "开启状态 1:启用,0未启用", position = 4,required = true,allowableValues = "0,1")
    @NotNull
    private Boolean status;

}
