package com.cdzg.xzshop.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "退货信息")
public class ReturnGoodsInfoVo {

    /**
     * 退货收件人
     */
    @ApiModelProperty(value = "退货收件人",required = true,position = 1)
    @NotBlank
    @Size(max = 30)
    private String recipient;

    /**
     * 退货联系方式
     */
    @ApiModelProperty(value = "退货联系方式",required = true,position = 2)
    @NotBlank
    @Size(max = 30)
    private String phone;

    /**
     * 退货地址
     */
    @ApiModelProperty(value = "退货地址",required = true,position = 3)
    @NotBlank
    @Size(max = 30)
    private String address;

    /**
     * 退货注意事项
     */
    @ApiModelProperty(value = "退货注意事项",allowEmptyValue = true,position = 4)
    @Size(max = 200)
    private String precautions;
}
