package com.cdzg.xzshop.vo.app.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("退款-买家发货")
@Data
public class BuyerShipVO {

    @ApiModelProperty("退款单号")
    private Long id;

    @ApiModelProperty("物流公司")
    private String logisticsCompany;

    @ApiModelProperty("物流单号")
    private String logisticsNumber;
}
