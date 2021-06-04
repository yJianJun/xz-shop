package com.cdzg.xzshop.to.app;

import com.cdzg.xzshop.constant.ReceivePaymentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "业务退款To")
@Data
public class RefundTo {

    @ApiModelProperty(value = "支付平台交易号", required = true)
    private String tradeNo;

    @ApiModelProperty(value = "商城业务订单号", required = true)
    private String outTradeNo;

    @ApiModelProperty(value = "退款单号", required = true)
    private String outRequestNo;

    @ApiModelProperty(value = "退款总金额", required = true)
    private String refundFee;

    @ApiModelProperty(value = "支付平台类型", required = true)
    private ReceivePaymentType type;
}
