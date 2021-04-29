package com.cdzg.xzshop.vo.admin;

import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(description = "商品类别分页参数模型")
public class GoodsCategoryPageVo{


    @ApiModelProperty(value = "分类等级", position = 1,required = true,allowableValues = "1,2")
    @NotNull
    private Integer level;

    @ApiModelProperty(value = "商品类别名称", position = 2,allowEmptyValue = true)
    private String name;

}
