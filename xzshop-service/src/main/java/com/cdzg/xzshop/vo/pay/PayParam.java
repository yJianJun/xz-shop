package com.cdzg.xzshop.vo.pay;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("订单支付参数")
public class PayParam {


    @ApiModelProperty(value = "业务订单id", required = true, position = 1)
    @NotNull
    private Long orderId;


    @ApiModelProperty(value = "终端IP地址（微信支付时必传）",allowEmptyValue = true,position = 2)
    private String ip;

}
