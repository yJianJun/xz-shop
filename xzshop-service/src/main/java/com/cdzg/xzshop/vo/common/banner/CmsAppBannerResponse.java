package com.cdzg.xzshop.vo.common.banner;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName : CmsAppBannerResponse
 * @Description : banner详情
 * @Author : XLQ
 * @Date: 2021-01-15 10:33
 */

@Data
public class CmsAppBannerResponse implements Serializable {

    private static final long serialVersionUID = 4870879427556435362L;

    @ApiModelProperty("bannerId")
    private Long id;
    @ApiModelProperty("bannerId")
    private Long bannerId;
    @ApiModelProperty("banner图片链接")
    private String imageUrl;
    @ApiModelProperty("跳转类型1->不跳转;2->H5;3->原生app;4->文章（富文本编辑内容）")
    private Integer redirectType;
    @ApiModelProperty("针对不同类型跳转不同模块")
    private String redirectUrl;
    @ApiModelProperty("banner排序")
    private Integer sequence;
    @ApiModelProperty("点击次数")
    private Integer clickCount;
    @ApiModelProperty("banner名称")
    private String name;
    @ApiModelProperty("模块选中标识")
    private String titleColor;
}
