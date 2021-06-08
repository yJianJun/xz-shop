package com.cdzg.xzshop.controller.app.pay;


import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.common.CommonResult;
import com.cdzg.xzshop.common.ResultCode;
import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.constant.PaymentMethod;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
import com.cdzg.xzshop.service.pay.PayService;
import com.cdzg.xzshop.vo.pay.PayParam;
import com.framework.utils.core.api.ApiResponse;
import com.google.gson.JsonObject;
import io.swagger.annotations.*;
import io.swagger.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.InetAddress;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("app/pay")
@Validated
@Api(tags = "app_支付相关")
public class PayController {

    @Autowired
    @Qualifier("weChatService")
    private PayService wxPayService;

    @Autowired
    @Qualifier("aliPayService")
    private PayService aliPayService;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private GoodsSpuService goodsSpuService;

    @ApiOperation("微信支付异步通知回调")
    @RequestMapping(value = "/wechat/notify", method = {RequestMethod.POST, RequestMethod.GET})
    @IgnoreAuth
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) {
        return wxPayService.callBack(request, response);
    }

    @ApiOperation("支付宝支付异步通知回调")
    @RequestMapping(value = "/ali/notify", method = {RequestMethod.POST, RequestMethod.GET})
    @IgnoreAuth
    public String aliNotify(HttpServletRequest request, HttpServletResponse response) {
        return aliPayService.callBack(request, response);
    }

    @MobileApi
    @PostMapping("/{type}")
    @ApiOperation("支付")
    public ApiResponse<String> pay(@ApiParam(value = "订单支付参数", required = true) @Valid @RequestBody PayParam payParam) throws Exception {
        Long orderId = payParam.getOrderId();
        PaymentMethod type = payParam.getType();

        Order order = orderService.getById(orderId);
        List<Long> itemsIds = orderItemService.findIdByOrderIdAndDeleted(orderId,0);

        if (Objects.isNull(order) || CollectionUtils.isEmpty(itemsIds)) {
            throw new BaseException("无此订单");
        }

        // 订单状态（1待付款2.待发货3.已发货4.已完成5.已关闭）
        Integer orderStatus = order.getOrderStatus();

        if (orderStatus != 1){
            throw new BaseException(ResultCode.Illegal);
        }

        List<GoodsSpu> spus = goodsSpuService.findBySpuNoInAndIsDeleteFalseAndStatusTrue(itemsIds);

        for (GoodsSpu goodsSpu : spus) {
            if (goodsSpu.getIsDelete()){
                throw new BaseException("此商品已删除");
            }
            if (!goodsSpu.getStatus()){
                throw new BaseException("此商品已下架");
            }
        }

        if (PaymentMethod.Wechat == type) {

            InetAddress addr = InetAddress.getLocalHost();
            String ip =addr.getHostAddress();
            return CommonResult.buildSuccessResponse(wxPayService.pay(ip,spus,order));
        } else {
            return CommonResult.buildSuccessResponse(aliPayService.pay(null, spus,order));
        }
    }
}
