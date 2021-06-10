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
import java.util.List;

/**
 * @ClassName : AppOrderDetailRespVO
 * @Description : app用户订单详情
 * @Author : EvilPet
 * @Date: 2021-06-10 14:49
 */
@Data
@ApiModel("app用户订单详情")
public class AppOrderDetailRespVO implements Serializable {
    private static final long serialVersionUID = -5026334512165454489L;

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
     * 订单类型 1-积分订单 2-普通订单
     */
    @ApiModelProperty(value = "订单类型 1-积分订单 2-普通订单")
    private Integer orderType;
    /**
     * 商品总价格（不含运费）单位：元
     */
    @ApiModelProperty(value = "商品总价格（不含运费）单位：元")
    private BigDecimal goodsPrice;
    /**
     * 运费  单位：元
     */
    @ApiModelProperty(value = "运费  单位：元")
    private BigDecimal freight;
    /**
     * 订单总金额(商品总额+运费) 单位：元
     */
    @ApiModelProperty(value = "订单总金额(商品总额+运费) 单位：元")
    private BigDecimal totalMoney;
    /**
     * 实际支付金额 单位：元
     */
    @ApiModelProperty(value = "实际支付金额 单位：元")
    private BigDecimal payMoney;
    /**
     * 备注(app用户下单备注)
     */
    @ApiModelProperty(value = "备注(app用户下单备注)")
    private String orderRemarks;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;

    @ApiModelProperty(value = "店铺name")
    private String shopName;

    /**
     * 订单状态（1待付款2.待发货3.已发货4.已完成5.已关闭）
     */
    @ApiModelProperty(value = "订单状态（1待付款2.待发货3.已发货4.已完成5.已关闭）")
    private Integer orderStatus;
    /**
     * 支付方式0-还未支付,1-支付宝,2-微信支付,3-积分
     */
    @ApiModelProperty(value = "支付方式0-还未支付,1-支付宝,2-微信支付,3-积分")
    private Integer payMethod;

    /**
     * 订单生成时间
     */
    @ApiModelProperty(value = "订单生成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 付款时间
     */
    @ApiModelProperty(value = "付款时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliverTime;
    /**
     * 成交时间(确认收货)
     */
    @ApiModelProperty(value = "成交时间(确认收货)")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dealTime;

    @ApiModelProperty(value = "倒计时时间戳，单位：ms")
    private Long remainingTime;

    @ApiModelProperty(value = "订单商品信息")
    List<OrderGoodsListRespVO> orderGoodsList;

    //////////////////物流收货信息/////////////////////

    /**
     * 物流公司编号（关联物流表）
     */
    @ApiModelProperty(value = "物流公司编号（关联物流表）")
    private String logisticsCompanyCode;
    /**
     * 物流编号(2.11新增)
     */
    @ApiModelProperty(value = "物流编号(2.11新增)")
    private String logisticsNumber;
    /**
     * 物流备注
     */
    @ApiModelProperty(value = "物流备注")
    private String logisticsRemarks;

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


}
