package com.cdzg.xzshop.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
    * 店铺详情
    */
@ApiModel(value="com-cdzg-xzshop-domain-ShopInfo")
@Data
@Builder
public class ShopInfo implements Serializable {
    /**
    * id
    */
    @ApiModelProperty(value="id")
    private Long id;

    /**
    * 店铺名称
    */
    @ApiModelProperty(value="店铺名称")
    private String shopName;

    /**
    * 所属工会
    */
    @ApiModelProperty(value="所属工会")
    private String union;

    /**
    * 运营部门
    */
    @ApiModelProperty(value="运营部门")
    private String department;

    /**
    * 店铺联系电话
    */
    @ApiModelProperty(value="店铺联系电话")
    private String phone;

    /**
    * 店铺log
    */
    @ApiModelProperty(value="店铺log")
    private String log;

    /**
    * 二级分类id
    */
    @ApiModelProperty(value="二级分类id")
    private Long categoryIdLevel2;

    /**
    * 一级分类id
    */
    @ApiModelProperty(value="一级分类id")
    private Long categoryIdLevel1;

    /**
    * 店铺上线时间
    */
    @ApiModelProperty(value="店铺上线时间")
    private LocalDateTime gmtPutOnTheShelf;

    /**
    * gmtCreate
    */
    @ApiModelProperty(value="gmtCreate")
    private LocalDateTime gmtCreate;

    /**
    * gmtUpdate
    */
    @ApiModelProperty(value="gmtUpdate")
    private LocalDateTime gmtUpdate;

    /**
    * 店铺联系人
    */
    @ApiModelProperty(value="店铺联系人")
    private String contactPerson;

    /**
    * 状态：1:上线,0未上线
    */
    @ApiModelProperty(value="状态：1:上线,0未上线")
    private Boolean status;

    /**
    * 店铺创建人
    */
    @ApiModelProperty(value="店铺创建人")
    private String createUser;

    /**
    * 运费
    */
    @ApiModelProperty(value="运费")
    private BigDecimal fare;

    private static final long serialVersionUID = 1L;
}