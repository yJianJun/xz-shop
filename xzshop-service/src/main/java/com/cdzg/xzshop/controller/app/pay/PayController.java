package com.cdzg.xzshop.controller.app.pay;


import com.cdzg.xzshop.config.annotations.api.IgnoreAuth;
import com.cdzg.xzshop.service.pay.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
}
