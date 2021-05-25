package com.cdzg.xzshop.componet.pay;

import com.baomidou.mybatisplus.extension.api.R;
import com.cdzg.xzshop.common.BaseException;
import com.cdzg.xzshop.config.pay.WechatPayConfig;
import com.cdzg.xzshop.constant.ReceivePaymentType;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.domain.ReceivePaymentInfo;
import com.cdzg.xzshop.mapper.OrderMapper;
import com.cdzg.xzshop.mapper.ReceivePaymentInfoMapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;
import java.util.SortedMap;

@Component
@Slf4j
public class PayClientUtils {



    private static OrderMapper orderMapper;

    private static ReceivePaymentInfoMapper paymentInfoMapper;

    @Autowired
    public PayClientUtils(OrderMapper orderMapper,ReceivePaymentInfoMapper paymentInfoMapper) {
        PayClientUtils.orderMapper = orderMapper;
        PayClientUtils.paymentInfoMapper = paymentInfoMapper;
    }

    /**
     *
     * @param appId 设置微信公众号或者小程序等的appid
     * @param mchId 微信支付商户号
     * @param mchKey  微信支付商户密钥
     * @param keyPath p12证书文件的绝对路径或者以classpath:开头的类路径.
     * @return
     * @throws IOException
     */
    public static WxPayService getWxClient(String appId, String mchId,String mchKey,String keyPath){


        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(appId));
        payConfig.setMchId(StringUtils.trimToNull(mchId));
        payConfig.setMchKey(StringUtils.trimToNull(mchKey));
        payConfig.setNotifyUrl(StringUtils.trimToNull(WechatPayConfig.getNotifyUrl()));
        payConfig.setTradeType(StringUtils.trimToNull(WechatPayConfig.getTradeType()));
        //payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getSubAppId()));
        //payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(keyPath));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    public static WxPayService getWxClient(String out_trade_no) {

        // 1.商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号；
        Order order = orderMapper.findById(Long.parseLong(out_trade_no));
        if (Objects.isNull(order)) {

            log.error("订单支付失败 -> 系统不存在此交易订单！");
            throw new BaseException("系统不存在此交易订单");
        }

        ReceivePaymentInfo receivePaymentInfo = paymentInfoMapper.findOneByShopIdAndType(Long.parseLong(order.getShopId()), ReceivePaymentType.Wechat);
        return PayClientUtils.getWxClient(receivePaymentInfo.getAppid(), receivePaymentInfo.getMchid(), receivePaymentInfo.getPrivateKey(), receivePaymentInfo.getKeyPath());
    }
}
