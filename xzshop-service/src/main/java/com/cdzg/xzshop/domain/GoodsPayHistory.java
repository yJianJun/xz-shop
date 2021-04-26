package com.cdzg.xzshop.domain;

import com.cdzg.xzshop.constant.PaymentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * 商品支付历史
 */
@ApiModel(value = "商品支付历史")
@Data
@Builder
public class GoodsPayHistory implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private Long spuNo;

    /**
     * 1:积分换购 2:线上支付
     */
    @ApiModelProperty(value = "1:积分换购 2:线上支付")
    private PaymentType paymentMethod;

    /**
     * 商品支付金额
     */
    @ApiModelProperty(value = "商品支付金额")
    private BigDecimal spuAmount;

    /**
     * 商品个数
     */
    @ApiModelProperty(value = "商品个数")
    private Long number;

    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal theTotalAmountOfOrders;

    /**
     * 第三方支付平台参数信息
     */
    @ApiModelProperty(value = "第三方支付平台参数信息")
    private String payJson;

    /**
     * 数据产生时间
     */
    @ApiModelProperty(value = "数据产生时间")
    private LocalDateTime gmtCreate;

    /**
     * 数据修改时间
     */
    @ApiModelProperty(value = "数据修改时间")
    private LocalDateTime gmtUpdate;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}