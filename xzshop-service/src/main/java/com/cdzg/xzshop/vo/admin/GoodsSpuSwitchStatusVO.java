package com.cdzg.xzshop.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("商品批量上下架参数")
public class GoodsSpuSwitchStatusVO {


    @ApiModelProperty(value = "商品id")
    @NotEmpty
    private List<Long> list;

    @ApiModelProperty(value = "下架/上架",required = true,allowableValues = "true,false")
    @NotNull
    private Boolean flag;

}
