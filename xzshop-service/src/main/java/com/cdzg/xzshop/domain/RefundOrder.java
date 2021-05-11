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
     * 0撤销
     * 1申请退款
     * 2拒绝退货
     * 3买家待发货
     * 4卖家待收货
     * 5收货拒绝
     * 6拒绝退款
     * 7退款成功
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 补充凭证
     */
    private String img;
    /**
     * 组织架构id
     */
    private Long orgId;
    /**
     * 补充说明
     */
    private String returnExplain;
    /**
     * 退款原因
     */
    private String reason;
    /**
     * 退款记录id
     */
    private Long refundRecordId;
    /**
     * 物流公司
     */
    private String logisticsCompany;
    /**
     * 物流单号
     */
    private String logisticsNumber;

}
