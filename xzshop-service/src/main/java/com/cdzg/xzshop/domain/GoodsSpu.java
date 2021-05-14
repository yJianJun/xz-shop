package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.handler.ListTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;

/**
 * 商品spu
 */
@ApiModel(value = "商品spu")
@Data
@Builder
@Document(indexName = "goods_spu",type = "xz_shop")
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "goods_spu",autoResultMap = true)
public class GoodsSpu implements Serializable {
    /**
     * id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 商品编号，唯一
     */
    @TableField(value = "spu_no")
    @ApiModelProperty(value = "商品编号，唯一")
    @Field(name = "spu_no")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long spuNo;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    @ApiModelProperty(value = "商品名称")
    @Field(type = FieldType.Text,searchAnalyzer = "ik_max_word",analyzer = "ik_max_word",name = "goods_name")
    private String goodsName;

    /**
     * 商品广告词
     */
    @TableField(value = "ad_word")
    @ApiModelProperty(value = "商品广告词")
    @Field(type = FieldType.Text,searchAnalyzer = "ik_max_word",analyzer = "ik_max_word",name = "ad_word")
    private String adWord;

    /**
     * 原价
     */
    @TableField(value = "price")
    @ApiModelProperty(value = "原价")
    @Field("price")
    private BigDecimal price;

    /**
     * 售价
     */
    @TableField(value = "promotion_price")
    @ApiModelProperty(value = "售价")
    @Field("promotion_price")
    private BigDecimal promotionPrice;

    /**
     * 积分售价
     */
    @TableField(value = "fraction_price")
    @ApiModelProperty(value = "积分售价")
    @Field("fraction_price")
    private BigDecimal fractionPrice;

    /**
     * 二级分类id
     */
    @TableField(value = "category_id_level2")
    @Field("category_id_level2")
    @ApiModelProperty(value = "二级分类id")
    private Long categoryIdLevel2;

    /**
     * 一级分类id
     */
    @TableField(value = "category_id_level1")
    @ApiModelProperty(value = "一级分类id")
    @Field("category_id_level1")
    private Long categoryIdLevel1;

    /**
     * 支付方式:1:积分换购 2:线上支付
     */
    @TableField(value = "payment_method")
    @Field(name = "payment_method",type = FieldType.Integer)
    @ApiModelProperty(value = "支付方式:1:积分换购 2:线上支付")
    private PaymentType paymentMethod;

    /**
     * 商家id
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value = "商家id")
    @Field("shop_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long shopId;

    /**
     * 商品是否上架
     */
    @TableField(value = "`status`")
    @Field(name = "`status`",type = FieldType.Boolean)
    @ApiModelProperty(value = "商品是否上架")
    private Boolean status;

    /**
     * 数据是否删除
     */
    @TableField(value = "is_delete")
    @Field(name = "is_delete",type = FieldType.Boolean)
    @ApiModelProperty(value = "数据是否删除")
    private Boolean isDelete;

    /**
     * 商品库存
     */
    @TableField(value = "stock")
    @Field("stock")
    @ApiModelProperty(value = "商品库存")
    private Long stock;

    /**
     * 创建人用户ID
     */
    @TableField(value = "create_user")
    @Field("create_user")
    @ApiModelProperty(value = "创建人用户ID")
    private String createUser;

    /**
     * 商品上架时间
     */
    @TableField(value = "gmt_put_on_the_shelf")
    @ApiModelProperty(value = "商品上架时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss",name = "gmt_put_on_the_shelf")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtPutOnTheShelf;

    /**
     * 数据产生时间
     */
    @TableField(value = "gmt_create")
    @ApiModelProperty(value = "数据产生时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss",name = "gmt_create")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtCreate;

    /**
     * 数据修改时间
     */
    @TableField(value = "gmt_update")
    @ApiModelProperty(value = "数据修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date,format = DateFormat.custom, pattern ="yyyy-MM-dd HH:mm:ss",name = "gmt_update")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtUpdate;

    /**
     * 商品展示图片
     */
    @TableField(value = "show_imgs",typeHandler = ListTypeHandler.class)
    @ApiModelProperty(value = "商品展示图片")
    @Field(name = "show_imgs",type = FieldType.Keyword)
    private List<String> showImgs;

    /**
     * 商品详情图片
     */
    @TableField(value = "desc_imgs")
    @ApiModelProperty(value = "商品详情图片")
    @Field("desc_imgs")
    private String descImgs;

    private static final long serialVersionUID = 1L;
}