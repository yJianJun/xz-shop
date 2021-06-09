package com.cdzg.xzshop.vo.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName : AppPayPointsReqVO
 * @Description : app用户商城积分消费扣除积分请求
 * @Author : EvilPet
 * @Date: 2021-06-09 10:35
 */

@Data
@ApiModel("app用户商城积分消费扣除积分请求")
public class AppPayPointsReqVO implements Serializable {

    private static final long serialVersionUID = 2024881606591606493L;

    @ApiModelProperty(value = "app用户id",required = true)
    private String customerId;

    @ApiModelProperty(value = "订单id",required = true)
    private String orderId;

    @ApiModelProperty(value = "商品名称",required = true)
    private String goodsName;

    @ApiModelProperty(value = "商品数量",required = true)
    private Integer goodsCount;

    @ApiModelProperty(value = "积分消费数量",required = true)
    private Integer pointsNumber;

}
