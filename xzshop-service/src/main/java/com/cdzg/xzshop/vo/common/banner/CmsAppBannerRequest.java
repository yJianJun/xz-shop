package com.cdzg.xzshop.vo.common.banner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName : CmsAppBannerRequest
 * @Description : banner请求信息
 * @Author : XLQ
 * @Date: 2021-01-15 10:35
 */

@Data
public class CmsAppBannerRequest implements Serializable {
    private static final long serialVersionUID = 3076483563543222293L;

    @ApiModelProperty("banner位置id")
    private String bannerGroupId;

    @ApiModelProperty("组织架构id")
    private String createBy;
}
