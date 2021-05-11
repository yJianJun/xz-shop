package com.cdzg.xzshop.to.admin;

import com.cdzg.xzshop.domain.GoodsSpu;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商品To")
@Builder
public class GoodsSpuTo extends GoodsSpu {


    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @ApiModelProperty(value = "销量", required = true)
    private Long sales;

}
