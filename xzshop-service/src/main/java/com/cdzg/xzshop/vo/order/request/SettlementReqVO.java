package com.cdzg.xzshop.vo.order.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName : SettlementReqVO
 * @Description : 结算请求参数
 * @Author : EvilPet
 * @Date: 2021-05-31 16:11
 */

@Data
@ApiModel("结算请求参数")
public class SettlementReqVO implements Serializable {

    private static final long serialVersionUID = 5741752831324499490L;

    @ApiModelProperty(value = "商品信息集合")
    private List<SettlementGoodsListReqVO> goodsList;


}
