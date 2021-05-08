package com.cdzg.xzshop.vo.admin;

import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商品分页参数模型")
public class GoodsSpuPageVo extends BasePageRequest {

    @ApiModelProperty(value = "商品状态:true-上架,false-下架,null-全部", position = 3, allowEmptyValue = true, allowableValues = "true,false,null")
    private Boolean status;

    @ApiModelProperty(value = "商品名称", position = 4,allowEmptyValue = true)
    private String goodsName;

    @ApiModelProperty(value = "上架开始时间", position = 5,allowEmptyValue = true,reference = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime start;

    @ApiModelProperty(value = "上架结束时间", position = 6,allowEmptyValue = true,reference = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime end;

    @ApiModelProperty(value = "商品编号", position = 7,allowEmptyValue = true)
    private Long spuNo;

    @ApiModelProperty(value = "一级分类id",position = 8,allowEmptyValue = true)
    private Long categoryIdLevel1;


    @ApiModelProperty(value = "二级分类id",position = 9,allowEmptyValue = true)
    private Long categoryIdLevel2;

}
