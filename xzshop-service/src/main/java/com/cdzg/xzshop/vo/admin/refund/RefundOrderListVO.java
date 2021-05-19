package com.cdzg.xzshop.vo.admin.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("退款订单列表")
public class RefundOrderListVO {

    /**
     * 退款编号
     */
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
    @ApiModelProperty(value = "订单编号")
    private Long orderId;
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
    private Integer goodsNumber;
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
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 申请退货时间
     */
    @ApiModelProperty(value = "申请退货时间")
    private LocalDateTime createTime;

}
