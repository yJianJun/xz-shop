package com.cdzg.xzshop.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "支付宝收款信息")
public class AliPayReceiveVo {


    //商户生成签名字符串所使用的签名算法类型
    @ApiModelProperty(value = "商户生成签名字符串所使用的签名算法类型", position =1,required = true,allowableValues = "RSA2,RSA")
    @NotEmpty
    private String signtype;

    //	支付宝分配给开发者的应用ID
    @ApiModelProperty(value = "支付宝分配给开发者的应用ID", position =2,required = true)
    @NotEmpty
    @Size(max = 50,message = "应用ID最大字符长度50")
    private String appId;

    //应用私钥 生成公钥时对应的私钥（填自己的）
    @ApiModelProperty(value = "应用私钥 生成公钥时对应的私钥（填自己的）", position =3,required = true)
    @NotEmpty
    private String privateKey;

    //支付宝公钥
    @ApiModelProperty(value = "支付宝公钥", position =4,required = true)
    @NotEmpty
    private String publicKey;

}
