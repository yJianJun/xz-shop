package com.cdzg.xzshop.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商城配置项")
public class SystemTimeConfigVO {

    /**
     * 用户下单后，订单未支付，取消时间设置（分钟）
     */
    @ApiModelProperty("用户下单后，订单未支付，取消时间设置（分钟）")
    private Integer cancelOrder;
    /**
     * 卖家发货后，买家未主动确认收货，确认收货时间设置（分钟）
     */
    @ApiModelProperty("卖家发货后，买家未主动确认收货，确认收货时间设置（分钟）")
    private Integer sureOrder;
    /**
     * 买家提交退款，卖家未处理，自动退款时间设置（分钟）
     */
    @ApiModelProperty("买家提交退款，卖家未处理，自动退款时间设置（分钟）")
    private Integer autoRefund;
    /**
     * 买家提交退货退款/换货申请，卖家未处理，系统自动处理时间设置（分钟）
     */
    @ApiModelProperty("买家提交退货退款/换货申请，卖家未处理，系统自动处理时间设置（分钟）")
    private Integer systemAutoDeal;
    /**
     * 卖家同意退货/换货，买家未处理，系统自动失败时间设置（分钟）
     */
    @ApiModelProperty("卖家同意退货/换货，买家未处理，系统自动失败时间设置（分钟）")
    private Integer systemAutoFail;
    /**
     * 卖家确认收货，未处理退款，系统自动退款时间设置（分钟）
     */
    @ApiModelProperty("卖家确认收货，未处理退款，系统自动退款时间设置（分钟）")
    private Integer systemAutoRefund;

}
