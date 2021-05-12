package com.cdzg.xzshop.vo.app.homepage;

import com.cdzg.xzshop.constant.PaymentType;
import com.cdzg.xzshop.vo.common.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商城首页商品列表分页模型")
public class GoodsSpuHomePageVo extends BasePageRequest {


    @ApiModelProperty(value = "支付方式:1:积分换购 2:线上支付", position = 3,required = true, allowableValues = "1,2")
    private PaymentType paymentType = PaymentType.Integral;

    @ApiModelProperty(value = "排序方式:false——按销量排序 true——按积分售价排序", position = 4,required = true, allowableValues = "true,false")
    private Boolean sort = false;
}
