package com.cdzg.xzshop.vo.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName : AdminUpdateTrainingCurriculumReqVO
 * @Description : 修改培训课程请求
 * @Author : XLQ
 * @Date: 2021-01-13 10:38
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "店铺编辑参数模型")
@Builder
public class ShopInfoUpdateVO extends ShopInfoAddVo {

    private static final long serialVersionUID = 1429523364761128797L;

    @ApiModelProperty(value = "店铺id", required = true, position = 10)
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "店铺id不能为空")
    private Long id;

}
