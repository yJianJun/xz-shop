package com.cdzg.xzshop.vo.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商品编辑参数模型")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GoodsSpuUpdateVO extends GoodsSpuAddVo {


    @ApiModelProperty(value = "商品编号", required = true, position = 13)
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "商品spuNo不能为空")
    private Long spuNo;
}
