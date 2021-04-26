package com.cdzg.xzshop.domain;

import com.cdzg.xzshop.constant.PaymentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
 * 商品spu
 */
@ApiModel(value = "商品spu")
@Data
@Builder
public class GoodsSpu implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 商品编号，唯一
     */
    @ApiModelProperty(value = "商品编号，唯一")
    private Long spuNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品广告词
     */
    @ApiModelProperty(value = "商品广告词")
    private String adWord;

    /**
     * 原价
     */
    @ApiModelProperty(value = "原价")
    private BigDecimal price;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价")
    private BigDecimal promotionPrice;

    /**
     * 积分售价
     */
    @ApiModelProperty(value = "积分售价")
    private BigDecimal fractionPrice;

    /**
     * 二级分类id
     */
    @ApiModelProperty(value = "二级分类id")
    private Long categoryIdLevel2;

    /**
     * 一级分类id
     */
    @ApiModelProperty(value = "一级分类id")
    private Long categoryIdLevel1;

    /**
     * 支付方式:1:积分换购 2:线上支付
     */
    @ApiModelProperty(value = "支付方式:1:积分换购 2:线上支付")
    private PaymentType paymentMethod;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long shopId;

    /**
     * 商品是否上架
     */
    @ApiModelProperty(value = "商品是否上架")
    private Boolean status;

    /**
     * 商品库存
     */
    @ApiModelProperty(value = "商品库存")
    private Long stock;

    /**
     * 创建人用户ID
     */
    @ApiModelProperty(value = "创建人用户ID")
    private String createUser;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    private String description;

    /**
     * 商品上架时间
     */
    @ApiModelProperty(value = "商品上架时间")
    private LocalDateTime gmtPutOnTheShelf;

    /**
     * gmtCreate
     */
    @ApiModelProperty(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    /**
     * gmtUpdate
     */
    @ApiModelProperty(value = "gmtUpdate")
    private Date gmtUpdate;

    private static final long serialVersionUID = 1L;
}