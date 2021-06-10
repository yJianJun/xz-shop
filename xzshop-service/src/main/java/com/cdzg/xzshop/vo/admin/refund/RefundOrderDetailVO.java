package com.cdzg.xzshop.vo.admin.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("退款订单详情")
public class RefundOrderDetailVO {

    /**
     * 退款id
     */
    @ApiModelProperty(value = "退款id")
    private Long id;
    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String userPhone;
    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;
    /**
     * 退款金额（元）
     */
    @ApiModelProperty(value = "退款金额（元）")
    private BigDecimal refundAmount;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
     * 退款商品信息列表
     */
    @ApiModelProperty(value = "退款商品信息列表")
    private List<GoodsInfoVO> goodsInfoVOS;
    /**
     * 支付方式 1:支付宝 2:微信
     */
    @ApiModelProperty(value = "支付方式 1:支付宝 2:微信")
    private Integer payType;
    /**
     * 退款类型 1退款 2退货退款
     */
    @ApiModelProperty(value = "退款类型 1退款 2退货退款")
    private Integer refundType;
    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderAmount;
    /**
     * 运费
     */
    @ApiModelProperty(value = "运费")
    private BigDecimal fare;
    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "商品总金额")
    private BigDecimal goodsAmount;
    /**
     * 状态
     * 0撤销
     * 1申请退货
     * 2拒绝退货
     * 3买家待发货
     * 4卖家待收货
     * 5拒绝签收
     * 6未收到货
     * 7申请退款
     * 8拒绝退款
     * 9退款成功
     */
    @ApiModelProperty(value = "状态 1申请退货 2拒绝退货 3买家待发货 4卖家待收货 5收货拒绝 6未收到货 7申请退款 8拒绝退款 9退款成功")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;
    /**
     * 补充凭证
     */
    @ApiModelProperty(value = "补充凭证")
    private String img;
    /**
     * 补充说明
     */
    @ApiModelProperty(value = "补充说明")
    private String returnExplain;
    /**
     * 退款原因
     */
    @ApiModelProperty(value = "退款原因")
    private String reason;
    /**
     * 拒绝退货/退款原因
     */
    @ApiModelProperty(value = "拒绝退货/退款原因")
    private String refuseReason;
    /**
     * 卖家拒绝收货原因
     */
    @ApiModelProperty(value = "卖家拒绝收货原因")
    private String refuseReceiptReason;
    /**
     * 物流公司
     */
    @ApiModelProperty(value = "物流公司")
    private String logisticsCompany;
    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String logisticsNumber;
    /**
     * 处理退货时间
     */
    @ApiModelProperty(value = "处理退货时间")
    private String dealTime;
    /**
     * 买家发货时间
     */
    @ApiModelProperty(value = "买家发货时间")
    private String buyerShipTime;
    /**
     * 商家处理收货时间
     */
    @ApiModelProperty(value = "商家处理收货时间")
    private String sellerDealGoodsTime;
    /**
     * 商家处理退款时间
     */
    @ApiModelProperty(value = "商家处理退款时间")
    private String returnMoneyTime;

}
