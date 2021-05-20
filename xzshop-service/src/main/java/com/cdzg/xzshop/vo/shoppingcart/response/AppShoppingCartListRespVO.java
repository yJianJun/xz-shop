package com.cdzg.xzshop.vo.shoppingcart.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName : AppShoppingCartListRespVO
 * @Description : app用户查询购物车信息返回
 * @Author : EvilPet
 * @Date: 2021-04-28 10:12
 */

@Data
@ApiModel("app用户查询购物车信息")
public class AppShoppingCartListRespVO implements Serializable {

    private static final long serialVersionUID = -966449272391195185L;

    /**
     * 商铺名字
     */
    @ApiModelProperty(value = "商铺名字")
    private String shopName;

    /**
     * 商铺ID
     */
    @ApiModelProperty(value = "商铺ID")
    private String shopId;

    @ApiModelProperty(value = "购物车商品信息")
    private List<AppShoppingCartGoodsRespVO> goodsList;

}
