package com.cdzg.xzshop.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("退款订单统计")
public class RefundOrderStatisticVO {

    /**
     * 全部订单
     */
    @ApiModelProperty(value = "全部订单")
    private Long total;

    /**
     * 退货待处理
     */
    @ApiModelProperty(value = "退货待处理")
    private Long returnToDo;

    /**
     * 退款待处理
     */
    @ApiModelProperty(value = "退款待处理")
    private Long refundToDo;

    /**
     * 退款成功
     */
    @ApiModelProperty(value = "退款成功")
    private Long refundSuccess;

}
