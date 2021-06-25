package com.cdzg.xzshop.vo.admin;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@ApiModel(description = "店铺添加参数模型")
public class ShopInfoAddVo {

    @ApiModelProperty(value = "店铺所属工会", position = 1, required = true)
    @NotEmpty
    @Size(max = 50,message = "工会最大字符长度50")
    private String union;

    @ApiModelProperty(value = "店铺运营部门", position = 2, required = true)
    @NotEmpty
    @Size(max = 50,message = "部门最大字符长度50")
    private String department;

    @ApiModelProperty(value = "店鋪名称", position = 3, required = true)
    @NotEmpty
    @Size(max = 50,message = "店铺名称最大字符长度50")
    private String shopName;

    @ApiModelProperty(value = "店鋪logo Url", position = 4, required = true)
    @NotEmpty
    private String logoUrl;

    @ApiModelProperty(value = "店鋪联系人", position = 5, required = true)
    @NotEmpty
    @Size(max = 50,message = "联系人最大字符长度50")
    private String person;

    @ApiModelProperty(value = "店鋪联系方式", position = 6, required = true)
    @NotEmpty
    @Size(max = 50,message = "联系方式最大字符长度50")
    private String contact;

   // @Valid yjjtodo 添加级联校验 字段会是必传
   @ApiModelProperty(value = "支付宝收款信息", position = 7, allowEmptyValue = true)
    private AliPayReceiveVo aliPayVo;

    //@Valid
    @ApiModelProperty(value = "微信收款信息", position = 8, allowEmptyValue = true)
    private WeChatReceiveVo wxPayVo;


    @ApiModelProperty(value = "运费", position = 9, required = true)
    @NotNull
    private BigDecimal fare;

    @ApiModelProperty(value = "启用收款方式标识 1:支付宝 2:微信支付 3:全部", position = 10,required = true,allowableValues = "1,2,3")
    @NotNull
    private Integer receiveMoney;

    @ApiModelProperty(value = "退货信息", position = 11,required = true)
    @NotNull
    @Valid
    private ReturnGoodsInfoVo returnfoVo;

}
