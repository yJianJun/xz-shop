package com.cdzg.xzshop.domain;

import com.cdzg.xzshop.constant.ReceivePaymentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * 收款信息表
 */
@ApiModel(value = "收款信息表")
@Data
@Builder
public class ReceivePaymentInfo implements Serializable {
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
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID")
    private String appid;

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号")
    private String mchid;

    /**
     * 通知地址
     */
    @ApiModelProperty(value = "通知地址")
    private String notifyUrl;

    /**
     * 收款类型：1:支付宝支付 2:微信支付
     */
    @ApiModelProperty(value = "收款类型：1:支付宝支付 2:微信支付")
    private ReceivePaymentType type;

    /**
     * API证书路径
     */
    @ApiModelProperty(value = "API证书路径")
    private String keyPath;

    /**
     * 微信支付商户密钥/应用私钥 支付宝生成公钥时对应的私钥（填自己的）
     */
    @ApiModelProperty(value = "微信支付商户密钥/应用私钥 支付宝生成公钥时对应的私钥（填自己的）")
    private String privateKey;

    /**
     * 签名类型
     */
    @ApiModelProperty(value = "签名类型")
    private String signtype;

    /**
     * 支付宝公钥
     */
    @ApiModelProperty(value = "支付宝公钥")
    private String publicKey;

    private static final long serialVersionUID = 1L;
}