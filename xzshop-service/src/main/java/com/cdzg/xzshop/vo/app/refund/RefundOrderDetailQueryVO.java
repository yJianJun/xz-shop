package com.cdzg.xzshop.vo.app.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询退款详情")
public class RefundOrderDetailQueryVO {

    @ApiModelProperty("退款单id")
    private Long refundId;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("订单明细id")
    private Long orderItemId;
}
