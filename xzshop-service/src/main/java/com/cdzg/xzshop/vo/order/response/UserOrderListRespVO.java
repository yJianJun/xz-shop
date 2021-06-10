package com.cdzg.xzshop.vo.order.response;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName : UserOrderListRespVO
 * @Description : 用户订单列表数据
 * @Author : EvilPet
 * @Date: 2021-06-10 10:59
 */

@Data
@ApiModel("用户订单列表数据")
public class UserOrderListRespVO implements Serializable {

    private static final long serialVersionUID = 6943157863943220605L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private String id;

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
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopId;

    @ApiModelProperty(value = "店铺Name")
    private String shopName;

    /**
     * 订单生成时间
     */
    @ApiModelProperty(value = "订单生成时间")
    private Date createTime;
    /**
     * 订单状态（1待付款2.待发货3.已发货4.已完成5.已关闭）
     */
    @ApiModelProperty(value = "订单状态（1待付款2.待发货3.已发货(待收货)4.已完成5.已关闭）")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单商品信息")
    List<OrderGoodsListRespVO> orderGoodsList;
}
