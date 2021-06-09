package com.cdzg.xzshop.vo.order.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName : CommitOrderRespVO
 * @Description : 提交订单返回参数
 * @Author : EvilPet
 * @Date: 2021-06-01 14:47
 */

@Data
@ApiModel("提交订单返回参数")
public class CommitOrderRespVO implements Serializable {

    private static final long serialVersionUID = -7188569107086162443L;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "剩余时间,单位：ms")
    private Long remainingTime;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "商铺ID")
    private String shopId;

    @ApiModelProperty(value = "商铺Name")
    private String shopName;

}
