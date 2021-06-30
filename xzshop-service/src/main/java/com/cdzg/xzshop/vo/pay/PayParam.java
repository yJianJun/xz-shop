package com.cdzg.xzshop.vo.pay;


import com.cdzg.xzshop.enums.PaymentMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("订单支付参数")
public class PayParam {


    @ApiModelProperty(value = "业务订单id", required = true, position = 1)
    @NotNull
    private Long orderId;

    @ApiModelProperty(value = "支付方式", required = true, allowableValues = "1,2",position = 2)
    @NotNull
    private PaymentMethod type;

}
