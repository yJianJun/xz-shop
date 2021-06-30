package com.cdzg.xzshop.to.app;

import com.cdzg.xzshop.enums.ReceivePaymentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "订单查询To")
@Data
public class QueryOrderTo {


    @ApiModelProperty(value = "支付平台交易号", required = true)
    private String tradeNo;

    @ApiModelProperty(value = "商城业务订单号", required = true)
    private String outTradeNo;


    @ApiModelProperty(value = "交易状态: 交易创建,等待买家付款——WAIT_BUYER_PAY、NOTPAY; 未付款交易超时关闭,或支付完成后全额退款——TRADE_CLOSED、REVOKED;" +
            " 交易支付成功——TRADE_SUCCESS、SUCCESS; 交易结束,不可退款——TRADE_FINISHED、CLOSED; 转入退款——REFUND; 用户支付中——USERPAYING; 支付失败——PAYERROR",
            required = true,
            allowableValues = "WAIT_BUYER_PAY,TRADE_CLOSED,TRADE_SUCCESS,SUCCESS,TRADE_FINISHED,REFUND,NOTPAY,CLOSED,REVOKED,USERPAYING,PAYERROR")
    private String tradeStatus;

    @ApiModelProperty(value = "针对当前订单状态,提示下一步操作的指引",allowEmptyValue = true)
    private String tradeStatusPrompt;


    @ApiModelProperty(value = "交易的订单金额", required = true)
    private String totalAmount;

    @ApiModelProperty(value = "支付平台类型", required = true)
    private ReceivePaymentType type;

    @ApiModelProperty(value = "买家在支付平台的用户id",required = true)
    private String buyerUserId;
}
