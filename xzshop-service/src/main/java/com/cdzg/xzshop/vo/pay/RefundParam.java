package com.cdzg.xzshop.vo.pay;

import com.cdzg.xzshop.constant.PaymentMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("订单退款参数")
public class RefundParam {

    @ApiModelProperty(value = "支付宝/微信 交易号", required = true, position = 1)
    @NotBlank
    String transactionId;


    @ApiModelProperty(value = "商户订单号", required =true, position = 2)
    @NotNull
    Long orderno;


    @ApiModelProperty(value = "退款单号", required =true, position = 3)
    @NotNull
    Long refundId;

    @ApiModelProperty(value = "退款金额", required =true, position = 4,notes = "单位为元，支持2位小数")
    @NotBlank
    String refundFee;

    @ApiModelProperty(value = "支付方式", required =true, position = 5,allowableValues = "1,2")
    @NotNull
    PaymentMethod type;
}
