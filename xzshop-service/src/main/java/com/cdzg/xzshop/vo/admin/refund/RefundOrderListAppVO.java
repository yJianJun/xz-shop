package com.cdzg.xzshop.vo.admin.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("app退款订单列表")
public class RefundOrderListAppVO {

    /**
     * 退款编号
     */
    @ApiModelProperty(value = "退款编号")
    private Long id;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
     * 退款类型 1退款 2退货退款
     */
    @ApiModelProperty(value = "退款类型 1退款 2退货退款")
    private Integer refundType;
    /**
     * 商品列表
     */
    @ApiModelProperty(value = "商品列表")
    private List<GoodsInfoVO> goodsInfoVOList;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态 0撤销 1申请退货 2拒绝退货 3买家待发货 4卖家待收货 5收货拒绝 6未收到货 7退款处理中 8拒绝退款 9退款成功")
    private Integer status;

}
