package com.cdzg.xzshop.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName : BaseIdReqVO
 * @Description : id请求vo
 * @Author : XLQ
 * @Date: 2021-01-08 09:23
 */

@Data
@ApiModel("id请求基础vo")
public class BaseIdReqVO implements Serializable{

    private static final long serialVersionUID = -4328224449220309132L;

    @ApiModelProperty(value = "id",required = true)
    @NotNull(message = "id不能为空")
    private String id;
}
