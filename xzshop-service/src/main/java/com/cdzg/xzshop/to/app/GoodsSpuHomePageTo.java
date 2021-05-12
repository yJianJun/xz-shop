package com.cdzg.xzshop.to.app;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商城首页商品To")
@Data
public class GoodsSpuHomePageTo extends GoodsSpu {

    @ApiModelProperty(value = "销量", required = true)
    private Long sales;
}
