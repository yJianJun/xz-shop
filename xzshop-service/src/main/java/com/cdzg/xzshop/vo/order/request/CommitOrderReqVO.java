package com.cdzg.xzshop.vo.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName : CommitOrderReqVO
 * @Description : 提交订单请求参数
 * @Author : EvilPet
 * @Date: 2021-06-01 14:46
 */

@Data
@ApiModel("提交订单请求参数")
public class CommitOrderReqVO implements Serializable {
    private static final long serialVersionUID = -7987344927796601773L;

    /**
     * 订单类型 0-普通订单 1-积分订单
     */
    @ApiModelProperty(value = "订单类型 1-积分订单 2-普通订单")
    @NotNull(message = "orderType cannot be null")
    @Range(min = 1,max = 2,message = "订单类型只能为 1-积分订单 2-普通订单")
    private Integer orderType;

    /**
     * 商铺ID
     */
    @ApiModelProperty(value = "商铺ID")
    @NotBlank(message = "shopId cannot be null")
    private String shopId;


    /**
     * 商品信息集合
     */
    @Valid
    @ApiModelProperty(value = "商品信息集合")
    @Size(min = 1,message = "商品信息集合不能为空")
    private List<CommitOrderGoodsReqVO> goodsList;



    ////////////////////收货信息//////////////////////////

    @ApiModelProperty(value = "备注(app用户下单备注)")
    private String orderRemarks;

    /**
     * 省(收货地址)
     */
    @ApiModelProperty(value = "省(收货地址)")
    @NotBlank(message = "省(收货地址)不能为空！")
    private String province;
    /**
     * 市(收货地址)
     */
    @ApiModelProperty(value = "市(收货地址)")
    @NotBlank(message = "市(收货地址)不能为空！")
    private String city;
    /**
     * 区(收货地址)
     */
    @ApiModelProperty(value = "区(收货地址)")
    @NotBlank(message = "区(收货地址)不能为空！")
    private String area;
    /**
     * 详细地址(收货地址)
     */
    @ApiModelProperty(value = "详细地址(收货地址)")
    @NotBlank(message = "详细地址(收货地址)不能为空！")
    private String address;
    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    @NotBlank(message = "收货人姓名不能为空！")
    private String consigneeName;
    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    @NotBlank(message = "收货人电话不能为空！")
    private String consigneePhone;

    //////////////////金额小计/////////////////

    @ApiModelProperty(value = "店铺运费")
    @NotNull(message = "运费不能为空，积分商品0运费")
    private BigDecimal fare;

    @ApiModelProperty(value = "商品金额 商品总价格（不含运费）单位：元 or 个")
    @NotNull(message = "商品总价格不能为空")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "订单总金额合计(商品总额+运费) 单位：元 or 个")
    @NotNull(message = "合计不能为空")
    private BigDecimal totalMoney;

    /////////////////hidden/////////////

    @ApiModelProperty(value = "用户id",hidden = true)
    private Long customerId;

    @ApiModelProperty(value = "shopName",hidden = true)
    private String shopName;

    @ApiModelProperty(value = "customerAccount",hidden = true)
    private String customerAccount;

    /**
     *用户下单时的系统自动取消订单配置时间(分钟)
     */
    @ApiModelProperty(value = "用户下单时的系统自动取消订单配置时间(分钟)",hidden = true)
    private Integer sysCancelConfig;

}
