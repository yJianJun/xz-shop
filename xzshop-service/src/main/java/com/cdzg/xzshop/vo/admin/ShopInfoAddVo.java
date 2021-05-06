package com.cdzg.xzshop.vo.admin;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(description = "店铺添加参数模型")
public class ShopInfoAddVo {

    @ApiModelProperty(value = "店铺所属工会", position = 1, required = true)
    @NotEmpty
    private String union;

    @ApiModelProperty(value = "店铺运营部门", position = 2, required = true)
    @NotEmpty
    private String department;

    @ApiModelProperty(value = "店鋪名称", position = 3, required = true)
    @NotEmpty
    private String shopName;

    @ApiModelProperty(value = "店鋪logo Url", position = 4, required = true)
    @NotEmpty
    private String logoUrl;

    @ApiModelProperty(value = "店鋪联系人", position = 5, required = true)
    @NotEmpty
    private String person;

    @ApiModelProperty(value = "店鋪联系方式", position = 6, required = true)
    @NotEmpty
    private String contact;

    @ApiModelProperty(value = "支付宝收款信息", position = 7, allowEmptyValue = true)
    private AliPayReceiveVo aliPayVo;

    @ApiModelProperty(value = "微信收款信息", position = 8, allowEmptyValue = true)
    private WeChatReceiveVo wxPayVo;


    @ApiModelProperty(value = "运费", position = 9, required = true)
    @NotNull
    private BigDecimal fare;

}
