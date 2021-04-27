package com.cdzg.xzshop.vo.admin;

import com.cdzg.xzshop.vo.common.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "店铺分页参数模型")
public class ShopPageVo extends PageParam {

    @ApiModelProperty(value = "店铺上下线状态", position = 3, allowEmptyValue = true,
            reference = "true:上架,false:下架,null:全部", allowableValues = "true,false,null")
    private Boolean status;

    @ApiModelProperty(value = "店鋪名称", position = 4,allowEmptyValue = true)
    private String shopName;

    @ApiModelProperty(value = "入驻开始时间", position = 5,allowEmptyValue = true)
    private LocalDateTime start;

    @ApiModelProperty(value = "入驻结束时间", position = 6,allowEmptyValue = true)
    private LocalDateTime end;


}