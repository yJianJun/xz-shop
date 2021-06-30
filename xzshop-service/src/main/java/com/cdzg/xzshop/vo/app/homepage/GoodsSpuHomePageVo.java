package com.cdzg.xzshop.vo.app.homepage;

import com.cdzg.xzshop.enums.PaymentType;
import com.cdzg.xzshop.vo.common.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商城首页商品列表分页模型")
public class GoodsSpuHomePageVo extends BasePageRequest {


    @ApiModelProperty(value = "支付方式:1:积分换购 2:线上支付", position = 3,required = true, allowableValues = "1,2")
    private PaymentType paymentType = PaymentType.Integral;

    @ApiModelProperty(value = "false——降序 true——升序", position = 4,required = true, allowableValues = "true,false")
    private Boolean sort = false;

    @ApiModelProperty(value = "是否为综合", position = 5,required = true, allowableValues = "true,false")
    private Boolean type = true;
}
