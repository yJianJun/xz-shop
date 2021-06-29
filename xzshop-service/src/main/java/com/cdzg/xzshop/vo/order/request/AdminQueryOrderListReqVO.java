package com.cdzg.xzshop.vo.order.request;

import com.cdzg.xzshop.vo.common.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.framework.utils.validate.constraint.Range;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName : AdminQueryOrderListReqVO
 * @Description : admin查询订单列表请求
 * @Author : EvilPet
 * @Date: 2021-06-18 15:03
 */

@Data
@ApiModel("订单列表请求")
public class AdminQueryOrderListReqVO  extends BasePageRequest implements Serializable {
    private static final long serialVersionUID = 1922390968850382979L;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "用户账号(手机号)")
    private String customerAccount;

    /**
     * 订单状态（0-查询全部 1待付款2.待发货3.已发货4.已完成5.已关闭）
     */
    @ApiModelProperty(value = "订单状态（0-查询全部 1待付款2.待发货3.已发货4.已完成5.已关闭）")
    @Range(min = 0,max = 5,message = "订单状态只能为（0-查询全部 1待付款2.待发货3.已发货4.已完成5.已关闭）")
    private Integer orderStatus;

    @ApiModelProperty(value = "startTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "endTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "店铺id，不传",hidden = true)
    private List<Long> shopIds;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;


}
