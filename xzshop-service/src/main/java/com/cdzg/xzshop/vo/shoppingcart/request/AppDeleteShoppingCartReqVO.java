package com.cdzg.xzshop.vo.shoppingcart.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName : AppDeleteShoppingCartReqVO
 * @Description : app用户删除购物车商品请求
 * @Author : EvilPet
 * @Date: 2021-04-28 10:50
 */

@Data
@ApiModel("app用户删除购物车商品请求")
public class AppDeleteShoppingCartReqVO implements Serializable {

    @ApiModelProperty(value = "购物车ids",required = true)
    private List<String> shoppingCartIds;

}
