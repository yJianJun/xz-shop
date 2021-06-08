package com.cdzg.xzshop.vo.shoppingcart.response;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName : AppShoppingCartGoodsRespVO
 * @Description : 购物车商品信息
 * @Author : EvilPet
 * @Date: 2021-05-18 15:21
 */

@Data
@ApiModel("购物车商品信息")
public class AppShoppingCartGoodsRespVO implements Serializable {

    private static final long serialVersionUID = 6067996576171539291L;

    @ApiModelProperty(value = "商品id")
    private String goodsId;

    @ApiModelProperty(value = "商品编号")
    private String spuNo;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品图片")
    private String goodsImg;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer goodsNumber;

    /**
     * 原价
     */
    @ApiModelProperty(value = "原价(单价)")
    private BigDecimal price;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价(单价)")
    private BigDecimal promotionPrice;

    @ApiModelProperty(value = "商品当前状态 0-正常 1-库存不足 2-已下架或已删除")
    private Integer goodsStatus;

    @ApiModelProperty(value = "购物车id")
    private String shoppingCartId;

}
