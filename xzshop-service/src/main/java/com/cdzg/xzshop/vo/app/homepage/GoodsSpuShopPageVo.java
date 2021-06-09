package com.cdzg.xzshop.vo.app.homepage;

import com.cdzg.xzshop.vo.common.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "店铺商品列表分页模型")
public class GoodsSpuShopPageVo extends BasePageRequest {


    @ApiModelProperty(value = "店铺id", position = 3,required = true)
    @NotNull
    private Long shopId;
}
