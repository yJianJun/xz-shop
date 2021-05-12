package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cdzg.xzshop.constant.PaymentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 商品支付历史
 */
@ApiModel(value = "商品支付历史")
@Data
@Builder
@TableName(value = "goods_pay_history")
public class GoodsPayHistory implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 商品编号
     */
    @TableField(value = "spu_no")
    @ApiModelProperty(value = "商品编号")
    private Long spuNo;

    /**
     * 1:积分换购 2:线上支付
     */
    @TableField(value = "payment_method")
    @ApiModelProperty(value = "1:积分换购 2:线上支付")
    private PaymentType paymentMethod;

    /**
     * 订单支付金额
     */
    @TableField(value = "spu_amount")
    @ApiModelProperty(value = "订单支付金额")
    private BigDecimal spuAmount;

    /**
     * 商品个数
     */
    @TableField(value = "`number`")
    @ApiModelProperty(value = "商品个数")
    private Long number;

    /**
     * 订单编号
     */
    @TableField(value = "order_no")
    @ApiModelProperty(value = "订单编号")
    private Long orderNo;

    /**
     * 订单总金额
     */
    @TableField(value = "the_total_amount_of_orders")
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal theTotalAmountOfOrders;

    /**
     * 第三方支付平台参数信息
     */
    @TableField(value = "pay_json")
    @ApiModelProperty(value = "第三方支付平台参数信息")
    private String payJson;

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
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}