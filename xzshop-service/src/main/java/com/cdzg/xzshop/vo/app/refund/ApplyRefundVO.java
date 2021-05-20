package com.cdzg.xzshop.vo.app.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("申请退款/退货")
public class ApplyRefundVO {

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long orderId;
    /**
     * 订单明细id
     */
    @ApiModelProperty("订单明细id")
    private Long orderItemId;
    /**
     * 退款原因
     */
    @ApiModelProperty("退款原因")
    private String reason;
    /**
     * 补充凭证
     */
    @ApiModelProperty("补充凭证")
    private List<String> img;
    /**
     * 补充说明
     */
    @ApiModelProperty("补充说明")
    private String returnExplain;
    /**
     * 退款类型 0退款 1退货退款
     */
    @ApiModelProperty("退款类型 0退款 1退货退款")
    private Integer type;


}
