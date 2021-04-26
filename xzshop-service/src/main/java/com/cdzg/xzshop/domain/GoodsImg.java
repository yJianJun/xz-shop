package com.cdzg.xzshop.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
    * 商品图片
    */
@ApiModel(value="com-cdzg-xzshop-domain-GoodsImg")
@Data
@Builder
public class GoodsImg implements Serializable {
    /**
    * 主键（自增）
    */
    @ApiModelProperty(value="主键（自增）")
    private Long id;

    /**
    * 商品id
    */
    @ApiModelProperty(value="商品id")
    private Long goodsId;

    /**
    * 商品图片
    */
    @ApiModelProperty(value="商品图片")
    private String goodsPicture;

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
    * 图片位置  1：主图 0：非主图
    */
    @ApiModelProperty(value="图片位置  1：主图 0：非主图")
    private Boolean position;

    private static final long serialVersionUID = 1L;
}