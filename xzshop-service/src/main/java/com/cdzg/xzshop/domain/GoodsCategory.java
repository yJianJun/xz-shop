package com.cdzg.xzshop.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 商品分类表
 */
@ApiModel(value = "商品分类表")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtCreate;

    /**
     * gmtUpdate
     */
    @ApiModelProperty(value = "gmtUpdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime gmtUpdate;

    /**
     * 商品分类创建人
     */
    @ApiModelProperty(value = "商品分类创建人")
    private String createUser;

    private static final long serialVersionUID = 1L;
}