package com.cdzg.xzshop.vo.order.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName : OrderGoodsListRespVO
 * @Description : 订单商品信息
 * @Author : EvilPet
 * @Date: 2021-06-10 13:44
 */

@Data
@ApiModel("订单商品信息")
public class OrderGoodsListRespVO implements Serializable {

    private static final long serialVersionUID = -6275467083615527487L;

    @ApiModelProperty(value = "商品id")
    private String goodsId;

    @ApiModelProperty(value = "商品编号")
    private String spuNo;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品购买时单价")
    private BigDecimal goodsUnitPrice;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsCount;

    @ApiModelProperty(value = "商品图片")
    private String goodsImg;

    @ApiModelProperty(value = "状态 0正常 1.退款中2.已退款3.退货中4.已退货5.拒绝退款6.拒绝退货")
    private Integer status;
}
