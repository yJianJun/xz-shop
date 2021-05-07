package com.cdzg.xzshop.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款/退货订单
 */
@Data
public class RefundOrder {
    /**
     * 退款id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户账号
     */
    private String userPhone;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 订单商品项id
     */
    private Long orderItemId;
    /**
     * 退款金额（元）
     */
    private BigDecimal refundAmount;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private Integer goodsNumber;
    /**
     * 支付方式 1:支付宝 2:微信
     */
    private Integer payType;
    /**
     * 订单总金额
     */
    private BigDecimal orderAmount;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 补充凭证和说明
     */
    private String img;



}
