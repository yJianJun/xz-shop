package com.cdzg.xzshop.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("商品分类批量禁用启用参数")
public class GoodsCategorySwitchStatusVo {

    @ApiModelProperty(value = "商品分类id")
    @NotNull
    private List<Long> list;

    @ApiModelProperty(value = "禁用/启用",required = true,allowableValues = "true,false")
    @NotNull
    private Boolean flag;

}
