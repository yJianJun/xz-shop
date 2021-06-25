package com.cdzg.xzshop.vo.admin;

import com.cdzg.xzshop.constant.PaymentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
@Data
@ApiModel(description = "商品添加参数模型")
public class GoodsSpuAddVo {

    @ApiModelProperty(value = "商品名字", position = 1,required = true)
    @NotBlank
    private String goodsName;

    /**
     * 商品广告词
     */
    @ApiModelProperty(value = "商品广告词",position = 2,allowEmptyValue = true)
    @Size(max = 50,message = "商品广告词最大字符长度50个")
    private String adWord;

    /**
     * 原价
     */
    @ApiModelProperty(value = "原价",position = 3,allowEmptyValue = true)
    private BigDecimal price;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价",required = true,position = 4)
    @NotNull
    private BigDecimal promotionPrice;

    /**
     * 积分售价
     */
    @ApiModelProperty(value = "积分售价",required = true,position = 5)
    @NotNull
    private BigDecimal fractionPrice;

    @ApiModelProperty(value = "二级分类id",allowEmptyValue = true,position = 7)
    private Long categoryIdLevel2;

    @ApiModelProperty(value = "一级分类id",allowEmptyValue = true,position = 6)
    private Long categoryIdLevel1;

    @ApiModelProperty(value = "支付方式:1:积分换购 2:线上支付",required = true,position = 8)
    @NotNull
    private PaymentType paymentMethod;


    @ApiModelProperty(value = "商品是否上架",position = 9,required = true)
    @NotNull
    private Boolean status;


    @ApiModelProperty(value = "商品库存",position = 10,required = true)
    @NotNull
    private Long stock;

    @ApiModelProperty(value = "商品展示图片",position = 11,required = true)
    @NotEmpty
    @Size(max = 5)
    private List<String> showImgs;

    @ApiModelProperty(value = "商品详情图片",position = 12,required = true)
    @NotBlank
    @Size(max = 200)
    private String descImgs;
}
