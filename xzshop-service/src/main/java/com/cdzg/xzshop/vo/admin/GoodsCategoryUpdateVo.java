package com.cdzg.xzshop.vo.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商品分类编辑参数模型")
public class GoodsCategoryUpdateVo extends GoodsCategoryAddVo {

    @ApiModelProperty(value = "商品分类id", required = true, position = 5)
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "商品分类id不能为空")
    private Long id;
}
