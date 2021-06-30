package com.cdzg.xzshop.vo.order.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName : SettlementGoodsListRespVO
 * @Description : 结算接口返回商品信息
 * @Author : EvilPet
 * @Date: 2021-06-01 09:56
 */


@Data
@ApiModel("结算接口返回商品信息")
public class SettlementGoodsListRespVO implements Serializable {
    private static final long serialVersionUID = -2477482843520689448L;

    @ApiModelProperty(value = "商品id")
    private String id;

    /**
     * 商品编号，唯一
     */
    @ApiModelProperty(value = "商品编号")
    private String spuNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价(单位:元)")
    private BigDecimal promotionPrice;

    /**
     * 积分售价
     */
    @ApiModelProperty(value = "积分售价(单位:积分个数)")
    private BigDecimal fractionPrice;


    @ApiModelProperty(value = "商品库存")
    private Long stock;

    @ApiModelProperty(value = "商品图片")
    private String goodsImg;

}
