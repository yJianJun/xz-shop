package com.cdzg.xzshop.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("ids请求参数")
public class IdsReqVo {
    @ApiModelProperty("ids")
    private List<Long> ids;
}
