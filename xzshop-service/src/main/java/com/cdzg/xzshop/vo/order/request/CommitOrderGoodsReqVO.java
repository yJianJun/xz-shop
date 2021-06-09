package com.cdzg.xzshop.vo.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName : CommitOrderGoodsReqVO
 * @Description : 提交订单商品信息
 * @Author : EvilPet
 * @Date: 2021-06-01 15:47
 */
@Data
@ApiModel("订单商品信息")
public class CommitOrderGoodsReqVO implements Serializable {

    private static final long serialVersionUID = 5935906146917168357L;

    @ApiModelProperty(value = "商品id")
    @NotNull(message = "goodsId cannot be null")
    private String goodsId;

    @ApiModelProperty(value = "商品名称")
    @NotNull(message = "goodsName cannot be null")
    private String goodsName;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    @Range(min = 1,message = "goodsCount must be equal or greater than 1")
    private Integer goodsCount;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价(单价)")
    @NotNull(message = "商品单价不能为空")
    private BigDecimal promotionPrice;

    @ApiModelProperty(value = "购物车id,如果有，必传")
    private String shoppingCartId;


}
