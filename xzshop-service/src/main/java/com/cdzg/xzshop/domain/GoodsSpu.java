package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.handler.ListTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

/**
 * 商品spu
 */
@ApiModel(value = "商品spu")
@Data
@Builder
@TableName(autoResultMap = true)
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
    private LocalDateTime gmtUpdate;

    /**
     * showImgs
     */
    @Column(name = "show_imgs")
    @ApiModelProperty(value = "showImgs")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<String> showImgs;

    /**
     * descImgs
     */
    @Column(name = "desc_imgs")
    @ApiModelProperty(value = "descImgs")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<String> descImgs;

    private static final long serialVersionUID = 1L;
}