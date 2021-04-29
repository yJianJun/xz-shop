package com.cdzg.xzshop.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
    * 店铺图片
    */
@ApiModel(value="com-cdzg-xzshop-domain-ShopImg")
@Data
@Builder
public class ShopImg implements Serializable {
    /**
    * 主键（自增）
    */
    @ApiModelProperty(value="主键（自增）")
    private Long id;

    /**
    * 商铺id
    */
    @ApiModelProperty(value="商铺id")
    private Long shopId;

    /**
    * 店铺图片
    */
    @ApiModelProperty(value="店铺图片")
    private String shopPicture;

    /**
    * gmtCreate
    */
    @ApiModelProperty(value="gmtCreate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtCreate;

    /**
    * gmtUpdate
    */
    @ApiModelProperty(value="gmtUpdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtUpdate;

    /**
    * 图片位置  1：主图 0：非主图
    */
    @ApiModelProperty(value="图片位置  1：主图 0：非主图")
    private Boolean position;

    private static final long serialVersionUID = 1L;
}