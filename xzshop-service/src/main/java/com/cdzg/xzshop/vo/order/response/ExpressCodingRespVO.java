package com.cdzg.xzshop.vo.order.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName : ExpressCodingRespVO
 * @Description : 物流公司码表
 * @Author : EvilPet
 * @Date: 2021-06-21 13:49
 */
@Data
@ApiModel("物流公司码表")
public class ExpressCodingRespVO implements Serializable {
    private static final long serialVersionUID = 5069064479430792123L;

    /**
     * 快递名称
     */
    @ApiModelProperty(value = "快递名称")
    private String name;

    /**
     * 快递编号
     */
    @ApiModelProperty(value = "快递编号")
    private String logisticsCompanyCode;
}
