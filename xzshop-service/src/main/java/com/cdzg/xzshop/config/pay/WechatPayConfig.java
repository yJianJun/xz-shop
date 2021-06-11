package com.cdzg.xzshop.config.pay;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wxpay")
@Slf4j
public class WechatPayConfig {

    /**
     * 设置微信公众号或者小程序等的appid
     */
   // private static String appId = "wx00dc736050be0f82";

    /**
     * 微信支付商户号
     */
  //  private static String mchId = "1495430712";

    private static String notifyUrl = "https://47.105.37.49/xz-shop/app/pay/wechat/notify";

   // private static String keyPath = "classpath:pay/apiclient_cert.p12";

    /**
     * 微信支付商户密钥
     */
   // private static String mchKey = "0a3731beb7dbc726a712187bb7438ebc";

    // 下单 API 地址
 //   private static String placeUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private static String tradeType = "APP";

    public static String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        WechatPayConfig.tradeType = tradeType;
    }

    public static Logger getLog() {
        return log;
    }

    //public static String getAppId() {
    //    return appId;
    //}
    //
    //public void setAppid(String appid) {
    //    WechatPayConfig.appId = appid;
    //}
    //
    //public static String getMchId() {
    //    return mchId;
    //}
    //
    //public void setMch_id(String mch_id) {
    //    WechatPayConfig.mchId = mch_id;
    //}

    public static String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        WechatPayConfig.notifyUrl = notifyUrl;
    }

    //public static String getMchKey() {
    //    return mchKey;
    //}
    //
    //public void setKey(String key) {
    //    WechatPayConfig.mchKey = key;
    //}
    //
    //public static String getKeyPath() {
    //    return keyPath;
    //}
    //
    //public void setKeyPath(String keyPath) {
    //    WechatPayConfig.keyPath = keyPath;
    //}
    //
    //public static String getPlaceUrl() {
    //    return placeUrl;
    //}
    //
    //public void setPlaceUrl(String placeUrl) {
    //    WechatPayConfig.placeUrl = placeUrl;
    //}
}
