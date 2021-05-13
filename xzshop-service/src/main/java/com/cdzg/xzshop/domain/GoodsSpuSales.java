package com.cdzg.xzshop.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * 商品销量
 */
@ApiModel(value = "商品销量")
@Data
@Builder
@TableName(value = "goods_spu_sales")
public class GoodsSpuSales implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 商品编号
     */
    @TableField(value = "spu_no")
    @ApiModelProperty(value = "商品编号")
    private Long spuNo;

    /**
     * 销量
     */
    @TableField(value = "sales")
    @ApiModelProperty(value = "销量")
    private Long sales;

    /**
     * 数据产生时间
     */
    @TableField(value = "gmt_create")
    @ApiModelProperty(value = "数据产生时间")
    private LocalDateTime gmtCreate;

    /**
     * 数据修改时间
     */
    @TableField(value = "gmt_update")
    @ApiModelProperty(value = "数据修改时间")
    private LocalDateTime gmtUpdate;

    private static final long serialVersionUID = 1L;
}