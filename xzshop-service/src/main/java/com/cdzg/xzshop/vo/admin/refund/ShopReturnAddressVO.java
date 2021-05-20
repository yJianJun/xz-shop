package com.cdzg.xzshop.vo.admin.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("退货商家收货地址")
public class ShopReturnAddressVO {
    /**
     * 退货收件人
     */
    @ApiModelProperty(value = "退货收件人")
    private String recipient;

    /**
     * 退货联系方式
     */
    @ApiModelProperty(value = "退货联系方式")
    private String phone;

    /**
     * 退货地址
     */
    @ApiModelProperty(value = "退货地址")
    private String address;
}
