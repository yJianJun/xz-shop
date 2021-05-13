package com.cdzg.xzshop.to.app;

import com.cdzg.xzshop.domain.ShopInfo;
import com.cdzg.xzshop.to.admin.GoodsSpuTo;
import com.cdzg.xzshop.vo.admin.GoodsSpuPageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(description = "商品详情页To")
@Data
public class GoodsSpuDescriptionTo {

   @ApiModelProperty(value = "商品信息", required = true)
   private GoodsSpuHomePageTo spu;

   @ApiModelProperty(value = "店铺信息", required = true)
   private ShopInfo shopInfo;
}
