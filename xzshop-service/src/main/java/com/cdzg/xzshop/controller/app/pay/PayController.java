package com.cdzg.xzshop.controller.app.pay;


import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.config.annotations.api.MobileApi;
import com.cdzg.xzshop.service.pay.PayService;
import com.cdzg.xzshop.vo.pay.PayParam;
import com.cdzg.xzshop.vo.pay.RefundParam;
import com.framework.utils.core.api.ApiResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RequestMapping("app/pay")
@RestController
@Api(tags = "app_支付")
@Validated
public class PayController {

    @Autowired
    @Qualifier("weChatService")
    private PayService wxPayService;

    @Autowired
    @Qualifier("aliPayService")
    private PayService aliPayService;


    @MobileApi
    @PostMapping("/{type}")
    @ApiOperation("支付")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "path",
                    dataType = "String",
                    name = "type",
                    value = "支付方式",
                    required = true,
                    allowableValues = "wechat,ali"
            )
    })
    public ApiResponse<String> pay(@Valid @PathVariable("type") @NotBlank String type, @ApiParam(value = "订单支付参数", required = true) @Valid @RequestBody PayParam payParam) throws Exception {

        String orderId = payParam.getOrderID();
        //yjjtodo: 对订单号对应的订单数据 进行常规性校验 幂等
        /* 查询订单信息 */
//        OrderInfo info = orderInfoService.findOneByOrderNo(num);
//        OrderItem item = orderItemService.findOneByOrderNo(num);
//
//        if (Objects.isNull(info) || Objects.isNull(item)) {
//            return CommonResult.failed("无此订单！", ResultCode.FAILED.getCode());
////            throw ApiException.builder().errorCode(ResultCode.FAILED).message("无此订单！").build();
//        }
//
//        if (info.getStatus() != OrderStatus.Unpaid && info.getStatus() != OrderStatus.Payment_Failed) {
//            return CommonResult.failed(ResultCode.Illegal.getMessage(), ResultCode.Illegal.getCode());
////            throw ApiException.builder().errorCode(ResultCode.Illegal).build();
//        }
        //yjjtodo: 对此订单对应的商品 进行常规性校验
        //Voucher voucher = voucherService.findOneBySpuNoAndIsDeleteFalseAndIsExpiredFalse(item.getSpuId());
//        if (voucher.getIsDelete()) {
//            return CommonResult.failed("此商品已删除", ResultCode.FAILED.getCode());
////            throw ApiException.builder().errorCode(ResultCode.FAILED).message("此商品已删除").build();
//        }
//
//        if (voucher.getIsExpired()) {
//            return CommonResult.failed("此商品过期下架", ResultCode.FAILED.getCode());
//        }
        String body;
        if ("wechat".equals(type)) {

            String ip = payParam.getIp();
            body = wxPayService.pay(orderId, ip);
        } else {
            body = aliPayService.pay(orderId, null);
        }
        return ApiResponse.buildSuccessResponse(body);
    }


    @ApiOperation("查询交易订单信息")
    @MobileApi
    @GetMapping(value = "/{type}/query")
    public ApiResponse<String> query(@Valid @RequestParam("transactionId") @NotBlank @ApiParam(value = "微信/支付宝交易号", required = true) String transactionId,
                                     @Valid @RequestParam("outTradeNo") @NotBlank @ApiParam(value = "商品订单号", required = true) String outTradeNo,
                                     @Valid @PathVariable("type") @NotBlank @ApiParam(value = "支付方式", required = true, allowableValues = "wechat,ali") String type) throws Exception {

        String body;
        if ("wechat".equals(type)) {

            body = query(wxPayService, transactionId, outTradeNo);
        } else {
            body = query(aliPayService, transactionId, outTradeNo);
        }
        return ApiResponse.buildSuccessResponse(body);
    }

    /**
     * @param refundParam
     * @param type
     * @return
     * @throws Exception
     */
    @MobileApi
    @PostMapping("/{type}/refund")
    @ApiOperation("退款")
    public ApiResponse<String> refund(@Valid @RequestBody @ApiParam(value = "订单退款参数", required = true) RefundParam refundParam,
                                      @Valid @PathVariable("type") @NotBlank @ApiParam(value = "支付方式", required = true, allowableValues = "wechat,ali") String type
    ) throws Exception {
        String refundFee = refundParam.getRefundFee();
        String tradeno = refundParam.getTransactionId();
        String orderno = refundParam.getOrderno();

        //yjjtodo: 对订单号对应的订单数据 进行常规性校验 幂等
        /* 查询订单信息 */
//        OrderInfo info = orderInfoService.findOneByOrderNo(num);
//        OrderItem item = orderItemService.findOneByOrderNo(num);
//
//        if (Objects.isNull(info) || Objects.isNull(item)) {
//            return CommonResult.failed("无此订单！", ResultCode.FAILED.getCode());
////            throw ApiException.builder().errorCode(ResultCode.FAILED).message("无此订单！").build();
//        }
//
//        if (info.getStatus() != OrderStatus.Unpaid && info.getStatus() != OrderStatus.Payment_Failed) {
//            return CommonResult.failed(ResultCode.Illegal.getMessage(), ResultCode.Illegal.getCode());
////            throw ApiException.builder().errorCode(ResultCode.Illegal).build();
//        }
        //yjjtodo: 对此订单对应的商品 进行常规性校验
        //Voucher voucher = voucherService.findOneBySpuNoAndIsDeleteFalseAndIsExpiredFalse(item.getSpuId());
//        if (voucher.getIsDelete()) {
//            return CommonResult.failed("此商品已删除", ResultCode.FAILED.getCode());
////            throw ApiException.builder().errorCode(ResultCode.FAILED).message("此商品已删除").build();
//        }
//
//        if (voucher.getIsExpired()) {
//            return CommonResult.failed("此商品过期下架", ResultCode.FAILED.getCode());
//        }
        String body;
        if ("wechat".equals(type)) {

            body = refund(wxPayService, tradeno, orderno, refundFee);
        } else {
            body = refund(aliPayService, tradeno, orderno, refundFee);
        }
        return ApiResponse.buildSuccessResponse(body);
    }


    @ApiOperation("微信支付异步通知回调")
    @RequestMapping(value = "/wechat/pay/notify", method = {RequestMethod.POST, RequestMethod.GET})
    @IgnoreAuth
    public String wxNotify(HttpServletRequest request, HttpServletResponse response) {
        return wxPayService.callBack(request, response);
    }

    @ApiOperation("支付宝支付异步通知回调")
    @RequestMapping(value = "/ali/pay/notify", method = {RequestMethod.POST, RequestMethod.GET})
    @IgnoreAuth
    public String aliNotify(HttpServletRequest request, HttpServletResponse response) {
        return aliPayService.callBack(request, response);
    }

    String refund(PayService payService, String tradeno, String orderno, String refundFee) throws Exception {
        return payService.refund(tradeno, orderno, refundFee);
    }

    String query(PayService payService, String transactionId, String outTradeNo) throws Exception {
        return payService.query(transactionId, outTradeNo);
    }


}
