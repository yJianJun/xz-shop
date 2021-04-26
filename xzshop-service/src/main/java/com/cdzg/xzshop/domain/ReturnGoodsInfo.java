package com.cdzg.xzshop.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * 退货信息表
 */
@ApiModel(value = "退货信息表")
@Data
@Builder
public class ReturnGoodsInfo implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

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

    /**
     * 退货注意事项
     */
    @ApiModelProperty(value = "退货注意事项")
    private String precautions;

    private static final long serialVersionUID = 1L;
}