package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.handler.ListTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;

/**
 * 商品spu
 */
@ApiModel(value = "商品spu")
@Data
@Builder
@TableName(value = "goods_spu",autoResultMap = true)
public class GoodsSpu implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 商品编号，唯一
     */
    @TableField(value = "spu_no")
    @ApiModelProperty(value = "商品编号，唯一")
    private Long spuNo;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品广告词
     */
    @TableField(value = "ad_word")
    @ApiModelProperty(value = "商品广告词")
    private String adWord;

    /**
     * 原价
     */
    @TableField(value = "price")
    @ApiModelProperty(value = "原价")
    private BigDecimal price;

    /**
     * 售价
     */
    @TableField(value = "promotion_price")
    @ApiModelProperty(value = "售价")
    private BigDecimal promotionPrice;

    /**
     * 积分售价
     */
    @TableField(value = "fraction_price")
    @ApiModelProperty(value = "积分售价")
    private BigDecimal fractionPrice;

    /**
     * 二级分类id
     */
    @TableField(value = "category_id_level2")
    @ApiModelProperty(value = "二级分类id")
    private Long categoryIdLevel2;

    /**
     * 一级分类id
     */
    @TableField(value = "category_id_level1")
    @ApiModelProperty(value = "一级分类id")
    private Long categoryIdLevel1;

    /**
     * 支付方式:1:积分换购 2:线上支付
     */
    @TableField(value = "payment_method")
    @ApiModelProperty(value = "支付方式:1:积分换购 2:线上支付")
    private PaymentType paymentMethod;

    /**
     * 商家id
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value = "商家id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long shopId;

    /**
     * 商品是否上架
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value = "商品是否上架")
    private Boolean status;

    /**
     * 商品库存
     */
    @TableField(value = "stock")
    @ApiModelProperty(value = "商品库存")
    private Long stock;

    /**
     * 创建人用户ID
     */
    @TableField(value = "create_user")
    @ApiModelProperty(value = "创建人用户ID")
    private String createUser;

    /**
     * 商品上架时间
     */
    @TableField(value = "gmt_put_on_the_shelf")
    @ApiModelProperty(value = "商品上架时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtPutOnTheShelf;

    /**
     * 数据产生时间
     */
    @TableField(value = "gmt_create")
    @ApiModelProperty(value = "数据产生时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtCreate;

    /**
     * 数据修改时间
     */
    @TableField(value = "gmt_update")
    @ApiModelProperty(value = "数据修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtUpdate;

    /**
     * 商品展示图片
     */
    @TableField(value = "show_imgs",typeHandler = ListTypeHandler.class)
    @ApiModelProperty(value = "商品展示图片")
    private List<String> showImgs;

    /**
     * 商品详情图片
     */
    @TableField(value = "desc_imgs")
    @ApiModelProperty(value = "商品详情图片")
    private String descImgs;

    private static final long serialVersionUID = 1L;
}