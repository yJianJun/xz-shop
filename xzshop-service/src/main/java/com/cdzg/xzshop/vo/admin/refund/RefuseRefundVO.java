package com.cdzg.xzshop.vo.admin.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("拒绝退款")
public class RefuseRefundVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("拒绝原因")
    private String reason;
}
