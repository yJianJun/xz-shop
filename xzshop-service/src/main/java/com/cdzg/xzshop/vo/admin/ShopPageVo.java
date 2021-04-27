package com.cdzg.xzshop.vo.admin;

import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.cdzg.xzshop.vo.common.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "店铺分页参数模型")
public class ShopPageVo extends BasePageRequest {

    @ApiModelProperty(value = "店铺上下线状态", position = 3, allowEmptyValue = true,
            reference = "true:上架,false:下架,null:全部", allowableValues = "true,false,null")
    private Boolean status;

    @ApiModelProperty(value = "店鋪名称", position = 4,allowEmptyValue = true)
    private String shopName;

    @ApiModelProperty(value = "入驻开始时间", position = 5,allowEmptyValue = true,reference = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date start;

    @ApiModelProperty(value = "入驻结束时间", position = 6,allowEmptyValue = true,reference = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end;


}