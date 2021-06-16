package com.cdzg.xzshop.vo.admin.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("app退款订单详情")
public class RefundOrderDetailAppVO {

    /**
     * 退款id
     */
    @ApiModelProperty(value = "退款id")
    private Long id;
    /**
     * 退款金额（元）
     */
    @ApiModelProperty(value = "退款金额（元）")
    private BigDecimal refundAmount;
    /**
     * 退款商品信息列表
     */
    @ApiModelProperty(value = "退款商品信息列表")
    private List<GoodsInfoVO> goodsInfoVOS;
    /**
     * 退款类型 1退款 2退货退款
     */
    @ApiModelProperty(value = "退款类型 1退款 2退货退款")
    private Integer refundType;
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
    @ApiModelProperty(value = "状态 0撤销 1申请退货 2拒绝退货 3买家待发货 4卖家待收货 5收货拒绝 6未收到货 7申请退款 8拒绝退款 9退款成功")
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
     * 退货商家收货地址
     */
    @ApiModelProperty(value = "退货商家收货地址")
    private ShopReturnAddressVO shopReturnAddressVO;
    /**
     * 剩余时间
     */
    @ApiModelProperty(value = "剩余时间(分钟)")
    private Long restTime;

    @ApiModelProperty(value = "处理流程的状态")
    private List<Integer> processList;

}
