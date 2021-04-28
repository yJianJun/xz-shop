package com.cdzg.xzshop.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商城时间配置项
 */
@Data
public class SystemTimeConfig {

    private Long id;
    /**
     * 用户下单后，订单未支付，取消时间设置（小时）
     */
    private Integer cancelOrder;
    /**
     * 卖家发货后，买家未主动确认收货，确认收货时间设置（小时）
     */
    private Integer sureOrder;
    /**
     * 买家提交退款，卖家未处理，自动退款时间设置（小时）
     */
    private Integer autoRefund;
    /**
     * 买家提交退货退款/换货申请，卖家未处理，系统自动处理时间设置（小时）
     */
    private Integer systemAutoDeal;
    /**
     * 卖家同意退货/换货，买家未处理，系统自动失败时间设置（小时）
     */
    private Integer systemAutoFail;
    /**
     * 卖家确认收货，未处理退款，系统自动退款时间设置（小时）
     */
    private Integer systemAutoRefund;
    /**
     * 修改人
     */
    private Long updateBy;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}
