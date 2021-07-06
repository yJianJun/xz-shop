package com.cdzg.xzshop.config.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Description: 支付宝支付配置文件
 * @author: Administrator
 * @date: 2019/6/11 17:01
 */
@ConfigurationProperties(prefix = "alipay")
@Configuration
@Slf4j
public class AlipayConfig {


    //异步回调接口:得放到服务器上，且使用域名解析 IP
    //https://api.xx.com/receive_notify.htm
    //http://api.test.alipay.net/atinterface/receive_notify.htm
    private static String notifyUrl;

    //支付宝网关（注意沙箱alipaydev，正式则为 alipay）不需要修改
    private static String serverUrl = "https://openapi.alipay.com/gateway.do";
    //编码类型
    private static String charset = "utf-8";
    //数据类型
    private static String format = "json";
    //签名类型
    //private static String signtype = "RSA2";

    //支付宝应用id
  //  private static String APP_ID = "2018102961934152";

    //应用私钥 生成公钥时对应的私钥（填自己的）
   // private static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCcrPG6Y5WerIzHTe8AX76ClgkKPoO5mlRzHnUWLWe2nO66SoH6L2Hwt7cL8Ajng8kCNb8vOGV1HITqjfXYHjoAM2dzLi0N3Xc0LCxUICgSRsgRDrZ25qe6J4CBQHeLo5HqEp27NLA/8/RmA2ymY5fhduKlQ77AMQ7cy9zJK9EtCv3Ij79kmUQylNXlDNHePa7clB0YVRvXA4uYCsQWVLtrDCJJr7Zn8eaUDRBp5u5uaUaRpE3P58dPRUCWlo//WqP2kxd/ATulKMG310wLTJHe5x0+dfCHtb3rvs2IR8+Sx6e3H9sBimh/hAt4IyqLkkQw1z1APeCGhlSGNNxoHbU/AgMBAAECggEAGOJbDliMxgPv5CPbpvqN/p2n1+mv8EWzzgjzXc9eXqG0RQBafosSCjKTryCzuVm0DlbNA8n4S055Seu8NVQvsCJ59tBJtZ8MNMwjSkVFOAnn8tWcY2wlDasCfQTpN9OF9GB6juZCbBXqJl263v6Ie/Xu74LABDSGgMkcSxNbiMSqo5HNuka9mECaiNQrM5Nkpcvwj9bf44OQ3yP/tgyfIKpU+m15f6rGRCChoCWhuSuDhN+J5YRxFUkgcqSe3zKWV25mpBk4xfc1Alvf346zQaDI880I52UMIWsSrkLXGSfXzC5vKt8GHn8ma08R9OYy3BMyKuY4qC6/EOEeYlRTgQKBgQDskcXigG0aoibsa22zqrtxuG6xePCKT/HOHUN2wfiIjRr9QHxDJW4735w/QtYxb/qov3xJqbwZ6H9hymum7nwKX9ZJg3fHUeHnYrZLEzXOcIV3sXlQfk/oguuF+Sw4aQmO9Z/eyEf1tAwig/zEgHx2nCf5TJeKhY05XMEdRjfRfwKBgQCpi0hw/rj9yW7/2Fc6ijaCEuQp1r5Rq/lPCOLsxZuZEq9dPUe3s6P15DR4GlG+YEL4OCWJsZYUzUBbAN8AFyeCGdrhpUOiiHMfc9/ZKkvErWpD0u7JMlkhUexWps29xqE5cNmlkjYzZyrCpj+Z3SO6r5CKm7ddsRsproBuQYh8QQKBgGl8itd32q6glWigcpdFzzVjPwCl4+RYx4LX66g9z/vbp6g7o+0w+Jl8GSjgPbBntUafs+UI6ZZH5N0PLLeP6gGaQGwhUjyvl2Lc0lpho4F9DRaVLXomCK2uCyV/OSeiNp/niLUHa4b1FOAjKIfanyUodm6kp4IeER397SKHmAYzAoGAVDGfssU71oPOhGzJvH9gXC3HcmN6tZIiRzZRgmULRPO7dCD3WWlzFavSxoXDbDffaeZv9bXjosbem5KOKzxUWU4/nOh18uOxz7+6PNy9LZk0eSPpgq5nBp3pMIq7Bu/5pk29N3SpR9OGr4zhbqYblRyUOdDkOTm3FHbcvmP6xEECgYEAgoExBrweEeqYmIN43EVO4ZDYgSAFjPtPsdxzmNf+gFlRGg+1xCH16zcIPjJoElmUddpQOaopqjqI5iBe1cApJhymqex0Rxt0Dt26UdDWNEHq9jF6CaqDfkqTOyikNswSSEdYDDHu+0B7LffX5GawO7Jx0me1schVdw2Nkm8iZck=";

    //支付宝公钥
   // private static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw36BnlLG8vW2aEruAa6uDCEbTzRO/hKogdGLlGI9oEigv9dsgkTtoxpsJ28wXk0a0eVbNfh+uNkEaBlkBjjpUsjivhtT4HYV+d8e8LsIqchbCQt+npWDetAa85GTmZ3g9hlThcQiSqg01/NltzCEpsExeGoHtu8K+MPGY2U/EAElgZz6NQ0+4UH7bd7Fce9qsYK62RBg7OrB5BNcr7APLK155bMwt6sgHUbUOYq8rbtvx1LN8NS0mMCsgMGlC3ztzHafLste+6bgq8wJLfosxhRznz5hsKn4E1UR9KHX+HTmOQJouvjNeRq3GMZVnKSNs+kkteQOjInlKnEx30K49QIDAQAB";

  //  private static AlipayClient ALIPAY_CLIENT;
    
    //static {
    //
    //    ALIPAY_CLIENT = new DefaultAlipayClient(serverUrl, APP_ID, APP_PRIVATE_KEY,format,charset, ALIPAY_PUBLIC_KEY,signtype);
    //}


    //public static AlipayClient buildAlipayClient() {
    //
    //    return ALIPAY_CLIENT;
    //}


    public static String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        AlipayConfig.notifyUrl = notifyUrl;
    }

    public static String getUrl() {
        return serverUrl;
    }

    public void setUrl(String url) {
        AlipayConfig.serverUrl = url;
    }

    public static String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        AlipayConfig.charset = charset;
    }

    public static String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        AlipayConfig.format = format;
    }

    //public static String getSigntype() {
    //    return signtype;
    //}
    //
    //public void setSigntype(String signtype) {
    //    AlipayConfig.signtype = signtype;
    //}

    public static Logger getLog() {
        return log;
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        AlipayConfig.serverUrl = serverUrl;
    }

    //public static String getAppId() {
    //    return APP_ID;
    //}
    //
    //public static String getAppPrivateKey() {
    //    return APP_PRIVATE_KEY;
    //}
    //
    //public static String getAlipayPublicKey() {
    //    return ALIPAY_PUBLIC_KEY;
    //}

    //public static AlipayClient getAlipayClient() {
    //    return ALIPAY_CLIENT;
    //}
}
