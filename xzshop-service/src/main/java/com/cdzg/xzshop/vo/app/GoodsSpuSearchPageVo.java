package com.cdzg.xzshop.vo.app;

import com.cdzg.xzshop.vo.common.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商品搜索列表分页模型")
public class GoodsSpuSearchPageVo extends BasePageRequest {

    @ApiModelProperty(value = "搜索关键词", position = 4,required = true)
    @NotBlank
    private String keyWord;
}
