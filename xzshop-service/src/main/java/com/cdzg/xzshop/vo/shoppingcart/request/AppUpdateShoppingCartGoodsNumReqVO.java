package com.cdzg.xzshop.vo.shoppingcart.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName : AppUpdateShoppingCartGoodsNumReqVO
 * @Description : app用户修改购物车商品数量请求
 * @Author : EvilPet
 * @Date: 2021-05-19 16:24
 */
@Data
@ApiModel("app用户修改购物车商品数量请求")
public class AppUpdateShoppingCartGoodsNumReqVO implements Serializable {
    private static final long serialVersionUID = -4741991896396934869L;

    @ApiModelProperty(value = "修改数量",required = true)
    @Range(min = 1,message = "修改数量必须大于等于1")
    private Integer updateNum;

    @ApiModelProperty(value = "购物车id")
    @NotNull(message = "购物车id不能为空")
    private String shoppingCartId;

    @ApiModelProperty(value = "商品id")
    @NotNull(message = "商品id不能为空")
    private String goodsId;

}
