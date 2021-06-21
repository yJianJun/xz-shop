package com.cdzg.xzshop.vo.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName : AdminDeliverReqVO
 * @Description : admin发货请求参数
 * @Author : EvilPet
 * @Date: 2021-06-21 09:55
 */

@Data
@ApiModel("admin发货请求参数")
public class AdminDeliverReqVO implements Serializable {
    private static final long serialVersionUID = 7477779665477799929L;

    @ApiModelProperty(value = "订单id",required = true)
    @NotNull(message = "orderId cannot be null!")
    private String orderId;

    /**
     * 物流公司
     */
    @ApiModelProperty(value = "物流公司code",required = true)
    @NotNull(message = "logisticsCompanyCode cannot be null!")
    private  String  logisticsCompanyCode;

    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号",required = true)
    @NotNull(message = "logisticsNumber cannot be null!")
    private  String  logisticsNumber;

    /**
     * 物流备注
     */
    @ApiModelProperty(value = "物流备注")
    private  String  logisticsRemarks;
}
