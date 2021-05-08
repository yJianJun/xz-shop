package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.TableField;
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
import javax.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 商品spu
 */
@ApiModel(value = "商品spu")
@Data
@Builder
@TableName(autoResultMap = true)
@Table(name = "goods_spu")
public class GoodsSpu implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 商品编号，唯一
     */
    @Column(name = "spu_no")
    @ApiModelProperty(value = "商品编号，唯一")
    private Long spuNo;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品广告词
     */
    @Column(name = "ad_word")
    @ApiModelProperty(value = "商品广告词")
    private String adWord;

    /**
     * 原价
     */
    @Column(name = "price")
    @ApiModelProperty(value = "原价")
    private BigDecimal price;

    /**
     * 售价
     */
    @Column(name = "promotion_price")
    @ApiModelProperty(value = "售价")
    private BigDecimal promotionPrice;

    /**
     * 积分售价
     */
    @Column(name = "fraction_price")
    @ApiModelProperty(value = "积分售价")
    private BigDecimal fractionPrice;

    /**
     * 二级分类id
     */
    @Column(name = "category_id_level2")
    @ApiModelProperty(value = "二级分类id")
    private Long categoryIdLevel2;

    /**
     * 一级分类id
     */
    @Column(name = "category_id_level1")
    @ApiModelProperty(value = "一级分类id")
    private Long categoryIdLevel1;

    /**
     * 支付方式:1:积分换购 2:线上支付
     */
    @Column(name = "payment_method")
    @ApiModelProperty(value = "支付方式:1:积分换购 2:线上支付")
    private PaymentType paymentMethod;

    /**
     * 商家id
     */
    @Column(name = "shop_id")
    @ApiModelProperty(value = "商家id")
    private Long shopId;

    /**
     * 商品是否上架
     */
    @Column(name = "`status`")
    @ApiModelProperty(value = "商品是否上架")
    private Boolean status;

    /**
     * 商品库存
     */
    @Column(name = "stock")
    @ApiModelProperty(value = "商品库存")
    private Long stock;

    /**
     * 创建人用户ID
     */
    @Column(name = "create_user")
    @ApiModelProperty(value = "创建人用户ID")
    private String createUser;

    /**
     * 商品上架时间
     */
    @Column(name = "gmt_put_on_the_shelf")
    @ApiModelProperty(value = "商品上架时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtPutOnTheShelf;

    /**
     * gmtCreate
     */
    @Column(name = "gmt_create")
    @ApiModelProperty(value = "gmtCreate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtCreate;

    /**
     * gmtUpdate
     */
    @Column(name = "gmt_update")
    @ApiModelProperty(value = "gmtUpdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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