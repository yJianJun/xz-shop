package com.cdzg.xzshop.vo.shoppingcart.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName : AddShoppingCartReqVO
 * @Description : 添加商品到购物车请求
 * @Author : EvilPet
 * @Date: 2021-05-18 09:35
 */

@Data
@ApiModel("添加商品到购物车请求")
public class AddShoppingCartReqVO implements Serializable {

    private static final long serialVersionUID = 4280041483675058218L;

    @ApiModelProperty(value = "商品id",required = true)
    @NotBlank(message = "goodsId cannot be null")
    private String goodsId;

    /**
     * 商品购买数量
     */
    @ApiModelProperty(value = "商品数量",required = true)
    @NotBlank(message = "goodsNumber cannot be null")
    @Min(value = 1,message = "goodsNumber must be greater than 1")
    private Integer goodsNumber;

}
