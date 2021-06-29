package com.cdzg.xzshop.vo.order.request;

import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.framework.utils.validate.constraint.Range;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName : AppQueryOrderListReqVO
 * @Description : 订单列表请求
 * @Author : EvilPet
 * @Date: 2021-06-10 11:00
 */

@Data
@ApiModel("订单列表请求")
public class AppQueryOrderListReqVO extends BasePageRequest implements Serializable {
    private static final long serialVersionUID = 4077543434845400499L;

    @ApiModelProperty(value = "订单状态（0-全部 1-待付款 2.待发货 3.待收货）")
    @NotNull(message = "orderStatus cannot be null")
    @Range(min = 0,max = 3,message = "订单状态只能为（0-全部 1-待付款 2.待发货 3.待收货）")
    private Integer orderStatus;

    @ApiModelProperty(value = "用户id")
    private String customerId;

}
