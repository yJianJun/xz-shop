package com.cdzg.xzshop.vo.admin;


import com.cdzg.xzshop.vo.common.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商品二级分类分页参数模型")
public class GoodsCategorySubPageVo extends BasePageRequest {

    @ApiModelProperty(value = "商品类别id", position = 1,required = true)
    private Long id;
}
