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
     * 店铺id
     */
    private Long shopId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private String goodsNumber;
    /**
     * 支付方式 1:支付宝 2:微信
     */
    private Integer payType;
    /**
     * 退款类型 1退款 2退货退款
     */
    private Integer refundType;
    /**
     * 订单总金额
     */
    private BigDecimal orderAmount;
    /**
     * 状态
     * 0撤销
     * 1申请退货
     * 2拒绝退货
     * 3买家待发货
     * 4卖家待收货
     * 5拒绝签收
     * 6未收到货
     * 7退款处理中
     * 8拒绝退款
     * 9退款成功
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
     * 拒绝退货/退款原因
     */
    private String refuseReason;
    /**
     * 卖家拒绝收货原因
     */
    private String refuseReceiptReason;
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
