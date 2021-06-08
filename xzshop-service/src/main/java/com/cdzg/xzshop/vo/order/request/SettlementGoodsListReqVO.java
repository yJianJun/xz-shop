package com.cdzg.xzshop.vo.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName : SettlementGoodsListReqVO
 * @Description : 结算商品信息
 * @Author : EvilPet
 * @Date: 2021-06-01 09:40
 */

@Data
@ApiModel("结算商品信息")
public class SettlementGoodsListReqVO implements Serializable {
    private static final long serialVersionUID = 2681994037884877181L;

    @ApiModelProperty(value = "商品Id")
    @NotNull(message = "goodsId cannot be null!")
    private String goodsId;

    @ApiModelProperty(value = "商品数量")
    @NotNull(message = "goodsNumber cannot be null!")
    @Range(min = 1,message = "goodsNumber must equal or greater than 1")
    private Integer goodsNumber;

    @ApiModelProperty(value = "购物车ID 有就必传传没有就不传")
    private String shoppingCartId;
}
