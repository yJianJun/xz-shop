package com.cdzg.xzshop.domain;

import lombok.Data;

/**
 * 退款记录
 */
@Data
public class RefundRecord {
    /**
     * 支付平台交易号
     */
    private String tradeNo;
    /**
     * 商城业务订单号
     */
    private String outTradeNo;
    /**
     * 退款单号
     */
    private String outRequestNo;
    /**
     * 退款总金额
     */
    private String refundFee;
    /**
     * 支付平台类型
     */
    private Integer type;
    /**
     * 退款是否成功
     */
    private boolean status;
    /**
     * 错误信息描述,退款失败时有值
     */
    private String errCodeDesc;

}
