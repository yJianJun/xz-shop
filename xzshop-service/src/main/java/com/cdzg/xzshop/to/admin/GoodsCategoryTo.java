package com.cdzg.xzshop.to.admin;

import com.cdzg.xzshop.domain.GoodsCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商品分类To")
public class GoodsCategoryTo extends GoodsCategory {

    @ApiModelProperty(value = "店铺信息",required = true)
    private Boolean hasChildren;
}
