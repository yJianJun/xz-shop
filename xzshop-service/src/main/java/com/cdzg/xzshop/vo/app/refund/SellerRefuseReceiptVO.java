package com.cdzg.xzshop.vo.app.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("卖家拒绝收货")
public class SellerRefuseReceiptVO {

    @ApiModelProperty("退款id")
    private Long id;

    @ApiModelProperty("拒绝原因")
    private String refuseReason;
}
