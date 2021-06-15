package com.cdzg.xzshop.vo.admin.refund;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("退款数据")
public class RefundOrderExportVO extends BaseRowModel {

    /**
     * 退款编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "退款编号")
    @ExcelProperty(value = "退款编号", index = 0)
    private String id;
    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    @ExcelProperty(value = "用户账号", index = 1)
    private String userPhone;
    /**
     * 订单编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "订单编号")
    @ExcelProperty(value = "订单编号", index = 2)
    private String orderId;
    /**
     * 退款类型 1退款 2退货退款
     */
    @ApiModelProperty(value = "退款类型 1退款 2退货退款")
    @ExcelProperty(value = "退款类型", index = 3)
    private String refundType;
    /**
     * 退款金额（元）
     */
    @ApiModelProperty(value = "退款金额")
    @ExcelProperty(value = "退款金额", index = 4)
    private BigDecimal refundAmount;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    @ExcelProperty(value = "店铺名称", index = 5)
    private String shopName;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    @ExcelProperty(value = "商品名称", index = 6)
    private String goodsName;
    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    @ExcelProperty(value = "商品数量", index = 7)
    private String goodsNumber;
    /**
     * 支付方式 1:支付宝 2:微信
     */
    @ApiModelProperty(value = "支付方式 1:支付宝 2:微信")
    @ExcelProperty(value = "支付方式", index = 8)
    private Integer payType;
    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额")
    @ExcelProperty(value = "订单总金额", index = 9)
    private BigDecimal orderAmount;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态 1申请退货 2拒绝退货 3买家待发货 4卖家待收货 5收货拒绝 6未收到货 7申请退款 8拒绝退款 9退款成功")
    @ExcelProperty(value = "状态", index = 10)
    private String status;
    /**
     * 申请退货时间
     */
    @ApiModelProperty(value = "申请退货时间")
    @ExcelProperty(value = "申请退货时间", index = 11)
    private String createTime;

}