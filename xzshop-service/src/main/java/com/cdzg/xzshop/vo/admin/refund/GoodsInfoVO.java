package com.cdzg.xzshop.vo.admin.refund;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("退款订单商品列表")
public class GoodsInfoVO {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    /**
     * 商品spuNo
     */
    @ApiModelProperty(value = "商品spuNo")
    private Long spuNo;
    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String img;
    /**
     * 状态商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer goodsCount;
    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal goodsUnitPrice;
    /**
     * 小计
     */
    @ApiModelProperty(value = "小计")
    private BigDecimal totalPrice;

}
