package com.cdzg.xzshop.to.admin;

import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.domain.ReturnGoodsInfo;
import com.cdzg.xzshop.domain.ShopInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel(description = "店铺详情To")
public class ShopInfoDetailTo {

    @ApiModelProperty(value = "店铺信息", position = 1,required = true)
    private ShopInfo shopInfo;

    @ApiModelProperty(value = "店铺收款信息集合 微信&支付宝", position = 2,required = true)
    private List<ReceivePaymentInfo> payments;

    @ApiModelProperty(value = "店铺退货信息", position = 3,required = true)
    private ReturnGoodsInfo returnGoodsInfo;
}
