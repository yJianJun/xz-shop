package com.cdzg.xzshop.vo.order.response;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName : AdminOrderListRespVO
 * @Description : admin查询订单列表数据
 * @Author : EvilPet
 * @Date: 2021-06-18 15:02
 */
@Data
@ApiModel("订单列表数据")
public class AdminOrderListRespVO  implements Serializable {
    private static final long serialVersionUID = 5187742706061599212L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private String id;
    /**
     * app用户id
     */
    @ApiModelProperty(value = "app用户id")
    private String customerId;

    /**
     * 用户账号（手机号）
     */
    @ApiModelProperty(value = "用户账号（手机号）")
    private String customerAccount;

    /**
     * 订单类型 1-积分订单 2-普通订单
     */
    @ApiModelProperty(value = "订单类型 1-积分订单 2-普通订单")
    private Integer orderType;

    /**
     * 支付方式0-还未支付,1-支付宝,2-微信支付,3-积分
     */
    @ApiModelProperty(value = "支付方式0-还未支付,1-支付宝,2-微信支付,3-积分")
    private Integer payMethod;

    /**
     * 实际支付金额 单位：元
     */
    @ApiModelProperty(value = "实际支付金额 单位：元或积分个数")
    private String payMoney;

    /**
     * 订单状态（1待付款2.待发货3.已发货4.已完成5.已关闭）
     */
    @ApiModelProperty(value = "订单状态(1待付款2.待发货3.已发货4.已完成5.已关闭)")
    private Integer orderStatus;

    /**
     * 省(收货地址)
     */
    @ApiModelProperty(value = "省(收货地址)")
    private String province;
    /**
     * 市(收货地址)
     */
    @ApiModelProperty(value = "市(收货地址)")
    private String city;
    /**
     * 区(收货地址)
     */
    @ApiModelProperty(value = "区(收货地址)")
    private String area;
    /**
     * 详细地址(收货地址)
     */
    @ApiModelProperty(value = "详细地址(收货地址)")
    private String address;
    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;
    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    private String consigneePhone;

    /**
     * 订单生成时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
