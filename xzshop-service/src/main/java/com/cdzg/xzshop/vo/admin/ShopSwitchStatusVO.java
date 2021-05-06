package com.cdzg.xzshop.vo.admin;

import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("店铺批量上下架参数")
public class ShopSwitchStatusVO {

    private static final long serialVersionUID = 1139868838434896803L;

    @ApiModelProperty(value = "店鋪id")
    @NotEmpty
    private List<Long> list;

    @ApiModelProperty(value = "下架/上架",required = true,allowableValues = "true,false")
    @NotNull
    private Boolean flag;

}

