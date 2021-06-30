package com.cdzg.xzshop.domain;

import com.cdzg.xzshop.enums.ReceivePaymentType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Builder;
import lombok.Data;

/**
 * 店铺收款信息表
 */
@ApiModel(value = "店铺收款信息表")
@Data
@Builder
@Table(name = "receive_payment_info")
public class ReceivePaymentInfo implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 店铺ID
     */
    @Column(name = "shop_id")
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    /**
     * 应用ID
     */
    @Column(name = "appid")
    @ApiModelProperty(value = "应用ID")
    private String appid;

    /**
     * 商户号
     */
    @Column(name = "mchid")
    @ApiModelProperty(value = "商户号")
    private String mchid;

    /**
     * 是否启用 1:启用,0未启用
     */
    @Column(name = "`status`")
    @ApiModelProperty(value = "是否启用 1:启用,0未启用")
    private Boolean status;

    /**
     * 收款类型：1:支付宝支付 2:微信支付
     */
    @Column(name = "`type`")
    @ApiModelProperty(value = "收款类型：1:支付宝支付 2:微信支付")
    private ReceivePaymentType type;

    /**
     * API证书路径
     */
    @Column(name = "key_path")
    @ApiModelProperty(value = "API证书路径")
    private String keyPath;

    /**
     * 微信支付商户密钥/应用私钥 支付宝生成公钥时对应的私钥（填自己的）
     */
    @Column(name = "private_key")
    @ApiModelProperty(value = "微信支付商户密钥/应用私钥 支付宝生成公钥时对应的私钥（填自己的）")
    private String privateKey;

    /**
     * 签名类型
     */
    @Column(name = "signtype")
    @ApiModelProperty(value = "签名类型")
    private String signtype;

    /**
     * 支付宝公钥
     */
    @Column(name = "public_key")
    @ApiModelProperty(value = "支付宝公钥")
    private String publicKey;

    private static final long serialVersionUID = 1L;
}