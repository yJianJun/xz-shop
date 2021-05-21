package com.cdzg.xzshop.service.pay.impl;

import com.cdzg.xzshop.componet.SnowflakeIdWorker;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.GoodsSpu;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.mapper.OrderMapper;
import com.cdzg.xzshop.mapper.ReceivePaymentInfoMapper;
import com.cdzg.xzshop.service.pay.PayService;
import com.cdzg.xzshop.utils.pay.WxUtils;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.common.collect.Lists;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service("weChatService")
@Slf4j
public class WeChatServiceImpl implements PayService {

    @Resource
    private WxPayService wxService;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ReceivePaymentInfoMapper paymentInfoMapper;


    /**
     * 当收到通知进行处理时，首先检查对应业务数据的状态，
     * 在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
     */
    @Override
    public String callBack(HttpServletRequest request, HttpServletResponse response) {

        final WxPayOrderNotifyResult notifyResult;
        try {
            String xml = WxUtils.readRequest(request);
            notifyResult = this.wxService.parseOrderNotifyResult(xml);
        } catch (Exception e) {
            log.error("微信支付验签失败:{}", Json.pretty(e.getMessage()));
            return WxPayNotifyResponse.fail(e.getMessage());
        }

        //验签通过
        String out_trade_no = notifyResult.getOutTradeNo();
        String totalFee = BaseWxPayResult.fenToYuan(notifyResult.getTotalFee());
        String appid = notifyResult.getAppid();

        // 1.商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号；
        Order order = orderMapper.findById(Long.parseLong(out_trade_no));
        if (Objects.isNull(order)) {

            log.error("订单支付失败 -> 系统不存在此交易订单！");
            return WxPayNotifyResponse.fail("系统不存在此交易订单");
        }

        // 2.过滤重复的通知结果数据。 订单状态（1待付款2.待发货3.已发货4.已完成5.已关闭）
        // 判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功
        Integer orderStatus = order.getOrderStatus();
        if (orderStatus != 1) {
            log.error("订单号:" + out_trade_no + "的订单状态异常！");
            return WxPayNotifyResponse.success("此订单已处理");
        }

        //3.校验返回的订单金额是否与商户侧的订单金额一致，防止数据泄漏导致出现“假通知”，造成资金损失。
        if (order.getPayMoney().compareTo(new BigDecimal(totalFee)) != 0) {
            log.error("订单号:" + out_trade_no + "金额:" + "与实际支付金额不等");
            return WxPayNotifyResponse.fail("订单金额与实际支付金额不等");
        }

        //4.验证 app_id 是否为该商户本身。
        ReceivePaymentInfo paymentInfo = paymentInfoMapper.findOneByShopIdAndType(Long.parseLong(order.getShopId()), ReceivePaymentType.Wechat);
        if (!paymentInfo.getAppid().equals(appid)) {
            log.error("订单号:" + out_trade_no + "对应店家收款信息与实际收款账号信息不符");
            return WxPayNotifyResponse.fail("店家收款信息与实际收款账号信息不符");
        }

        List<String> successStrings = Lists.newArrayList(WxPayConstants.ResultCode.SUCCESS, "");
        String resultCode = notifyResult.getResultCode();
        String returnCode = notifyResult.getReturnCode();

        //校验正向支付结果是否成功
        if (!successStrings.contains(StringUtils.trimToEmpty(returnCode).toUpperCase())
                || !successStrings.contains(StringUtils.trimToEmpty(resultCode).toUpperCase())) {

            //Todo:支付失败后的业务处理
            //  return updateRecord(info, false, receiveMap);
            String errCode = notifyResult.getErrCode();
            String errCodeDes = notifyResult.getErrCodeDes();
            log.error("微信支付失败，错误代码:{}，错误详情:{}", errCode, errCodeDes);
            return WxPayNotifyResponse.fail("失败");
        }else {
            // 可在此持久化微信传回的该 map 数据
            //Todo:支付成功后的业务处理
            //return updateRecord(info, true, receiveMap);
            return WxPayNotifyResponse.success("成功");
        }
    }


    /**
     * 查询微信支付订单
     *
     * @param transactionId 微信交易订单号
     * @param outTradeNo    商品订单号
     */
    @Override
    public String query(String transactionId, String outTradeNo) throws WxPayException {
        return Json.pretty(this.wxService.queryOrder(transactionId, outTradeNo));
    }

    /**
     * 统一下单(详见https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1)
     * 在发起微信支付前，需要调用统一下单接口，获取"预支付交易会话标识"
     * 接口地址：https://api.mch.weixin.qq.com/pay/unifiedorder
     *
     * @param orderId 商品订单号
     * @param spus
     * @param order
     */
    @Override
    public String pay(Long orderId, String ipAddress, List<GoodsSpu> spus, Order order) throws WxPayException {

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        // 测试时，将支付金额设置为 1 分钱
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder()
                .outTradeNo(orderId + "")
                .totalFee(1)  //todo:测试1分钱
                .body("西藏职工app-商城商品交易")
                .spbillCreateIp(ipAddress)
                .build();
        WxPayUnifiedOrderResult result = this.wxService.unifiedOrder(request);
        log.info("微信支付调用结果:{}", Json.pretty(result));
        return Json.pretty(result);
    }

    /**
     * 订单退款 需要双向证书验证
     *
     * @param tradeno 微信交易订单号
     * @param orderno 商品订单号
     */
    @Override
    public String refund(String orderno, String tradeno, String refundFee) throws WxPayException {

        long refundId = snowflakeIdWorker.nextId(); //yjjtodo 雪花算法生成退款交易号
        WxPayRefundRequest request = WxPayRefundRequest.newBuilder()
                .outRefundNo(Long.toString(refundId))
                .totalFee(1) //yjjtodo 订单金额 整数 分
                .refundFee(new BigDecimal(refundFee).intValue())
                .outTradeNo(orderno)
                .transactionId(tradeno).build();
        return Json.pretty(this.wxService.refund(request));
    }

}
