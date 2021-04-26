package com.cdzg.xzshop.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * 商品分类表
 */
@ApiModel(value = "商品分类表")
@Data
@Builder
public class GoodsCategory implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 分类等级
     */
    @ApiModelProperty(value = "分类等级")
    private Integer level;

    /**
     * 上1级分类id
     */
    @ApiModelProperty(value = "上1级分类id")
    private Long parentId;

    /**
     * 1:启用,0未启用
     */
    @ApiModelProperty(value = "1:启用,0未启用")
    private Boolean status;

    /**
     * gmtCreate
     */
    @ApiModelProperty(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    /**
     * gmtUpdate
     */
    @ApiModelProperty(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

    /**
     * 商品分类创建人
     */
    @ApiModelProperty(value = "商品分类创建人")
    private String createUser;

    private static final long serialVersionUID = 1L;
}