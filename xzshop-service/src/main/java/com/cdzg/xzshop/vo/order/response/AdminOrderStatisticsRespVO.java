package com.cdzg.xzshop.vo.order.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName : AdminOrderStatisticsRespVO
 * @Description : admin订单列表顶部统计
 * @Author : EvilPet
 * @Date: 2021-06-21 09:37
 */
@Data
@ApiModel("admin订单列表顶部统计")
public class AdminOrderStatisticsRespVO implements Serializable {
    private static final long serialVersionUID = 504863026017694193L;

    @ApiModelProperty(value = "订单总数")
    private Integer totalNum;

    @ApiModelProperty(value = "待发货数")
    private Integer waitNum;

    @ApiModelProperty(value = "已发货数")
    private Integer alreadyNum;
}
