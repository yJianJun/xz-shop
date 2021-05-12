package com.cdzg.xzshop.vo.app.homepage;

import com.cdzg.xzshop.vo.common.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName : AppTrainingHomePageReqVO
 * @Description : app职工培训首页请求
 * @Author : XLQ
 * @Date: 2021-01-18 10:16
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("app职工培训首页请求")
public class AppTrainingHomePageReqVO extends BasePageRequest {
    private static final long serialVersionUID = -3604678095415288308L;

    @ApiModelProperty("城市Name")
    private String cityName;

    @ApiModelProperty(value = "城市code",hidden = true)
    private String cityCode;
}
