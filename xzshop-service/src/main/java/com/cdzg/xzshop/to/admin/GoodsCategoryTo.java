package com.cdzg.xzshop.to.admin;

import com.cdzg.xzshop.domain.GoodsCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商品分类To")
public class GoodsCategoryTo extends GoodsCategory {

    @ApiModelProperty(value = "商品2级分类集合",required = true)
    private List<GoodsCategory> children;

    @ApiModelProperty(value = "商品分类创建人")
    private String createUser;
}
