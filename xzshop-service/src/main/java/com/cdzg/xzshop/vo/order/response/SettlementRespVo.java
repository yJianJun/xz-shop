package com.cdzg.xzshop.vo.order.response;

import com.cdzg.xzshop.enums.PaymentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName : SettlementRespVo
 * @Description : 结算接口返回
 * @Author : EvilPet
 * @Date: 2021-05-31 16:12
 */

@Data
@ApiModel("结算接口返回")
public class SettlementRespVo implements Serializable {
    private static final long serialVersionUID = 8543072621120334184L;

    @ApiModelProperty(value = "店铺id")
    private String shopId;

    @ApiModelProperty(value = "店铺name")
    private String shopName;

    @ApiModelProperty(value = "商品金额汇总(单位:元/积分个数)")
    private BigDecimal goodsTotal;

    @ApiModelProperty(value = "商品运费(单位:元/积分个数)")
    private BigDecimal goodsFreight;

    @ApiModelProperty(value = "小计(单位:元/积分个数)")
    private BigDecimal subtotal;

    @ApiModelProperty(value = "合计(单位:元/积分个数)")
    private BigDecimal total;

    @ApiModelProperty(value = "支付方式:1:积分换购 2:线上支付")
    private PaymentType paymentMethod;

    @ApiModelProperty(value = "用户积分余额，用于积分商品结算")
    private BigDecimal userPoints;

    @ApiModelProperty(value = "商品信息集合")
    private List<SettlementGoodsListRespVO> goodsList;


}
