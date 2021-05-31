package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;

/**
 * 订单支付历史记录
 */
@ApiModel(value = "订单支付历史记录")
@Data
@Builder
@TableName(value = "order_pay_history")
public class OrderPayHistory implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 订单编号
     */
    @TableField(value = "order_number")
    @ApiModelProperty(value = "订单编号")
    private Long orderNumber;

    /**
     * 支付平台交易单号
     */
    @TableField(value = "pay_number")
    @ApiModelProperty(value = "支付平台交易单号")
    private String payNumber;


    /**
     * 支付是否成功
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value = "支付是否成功")
    private Boolean status;

    /**
     * 支付类型：1:支付宝支付 2:微信支付
     */
    @ApiModelProperty(value = "支付类型：1:支付宝支付 2:微信支付")
    private ReceivePaymentType type;


    /**
     * 支付金额
     */
    @TableField(value = "payment_amount")
    @ApiModelProperty(value = "支付金额")
    private BigDecimal paymentAmount;

    /**
     * 订单总金额
     */
    @TableField(value = "the_total_amount_of_orders")
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal theTotalAmountOfOrders;

    /**
     * 数据产生时间
     */
    @TableField(value = "gmt_create")
    @ApiModelProperty(value = "数据产生时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtCreate;

    /**
     * 数据修改时间
     */
    @TableField(value = "gmt_update")
    @ApiModelProperty(value = "数据修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtUpdate;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}