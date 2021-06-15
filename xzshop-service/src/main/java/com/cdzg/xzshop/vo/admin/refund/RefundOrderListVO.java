package com.cdzg.xzshop.vo.admin.refund;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("退款订单列表")
public class RefundOrderListVO {

    /**
     * 退款编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "退款编号")
    private Long id;
    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String userPhone;
    /**
     * 订单编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "订单编号")
    private Long orderId;
    /**
     * 退款类型 1退款 2退货退款
     */
    @ApiModelProperty(value = "退款类型 1退款 2退货退款")
    private Integer refundType;
    /**
     * 退款金额（元）
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private String goodsNumber;
    /**
     * 支付方式 1:支付宝 2:微信
     */
    @ApiModelProperty(value = "支付方式 1:支付宝 2:微信")
    private Integer payType;
    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderAmount;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态 1申请退货 2拒绝退货 3买家待发货 4卖家待收货 5收货拒绝 6未收到货 7申请退款 8拒绝退款 9退款成功")
    private Integer status;
    /**
     * 申请退货时间
     */
    @ApiModelProperty(value = "申请退货时间")
    private String createTime;

}
