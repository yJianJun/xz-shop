package com.cdzg.xzshop.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "微信收款信息")
public class WeChatReceiveVo {

    //应用ID
    @ApiModelProperty(value = "应用ID", position = 1, required = true)
    @NotEmpty
    @Size(max = 50,message = "应用ID最大字符长度50")
    private String appId;

    //	微信支付分配的商户号
    @ApiModelProperty(value = "微信支付分配的商户号", position = 2, required = true)
    @NotEmpty
    @Size(max = 50,message = "商户号最大字符长度50")
    private String mchId;

    // API证书路径
    @ApiModelProperty(value = "API证书路径", position = 3, required = true)
    @NotEmpty
    private String keyPath;

    /**
     * 微信支付商户密钥
     */
    @ApiModelProperty(value = "微信支付商户密钥", position = 4, required = true)
    @NotEmpty
    private String privateKey;

}
