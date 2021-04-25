package com.cdzg.xzshop.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("app分页带城市切换基础vo")
public class BaseAppPageReqVo extends BasePageRequest implements Serializable {

    @ApiModelProperty(value = "城市id")
    private String city;

}
