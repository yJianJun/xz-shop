package com.cdzg.xzshop.config.pay;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Binary Wang
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties(WechatPayConfig.class)
@AllArgsConstructor
public class WxPayConfiguration {



  @Bean
  @ConditionalOnMissingBean
  public WxPayService wxService() {
    WxPayConfig payConfig = new WxPayConfig();
    payConfig.setAppId(StringUtils.trimToNull(WechatPayConfig.getAppId()));
    payConfig.setMchId(StringUtils.trimToNull(WechatPayConfig.getMchId()));
    payConfig.setMchKey(StringUtils.trimToNull(WechatPayConfig.getMchKey()));
    payConfig.setNotifyUrl(StringUtils.trimToNull(WechatPayConfig.getNotifyUrl()));
    payConfig.setTradeType(StringUtils.trimToNull(WechatPayConfig.getTradeType()));
    //payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getSubAppId()));
    //payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getSubMchId()));
    payConfig.setKeyPath(StringUtils.trimToNull(WechatPayConfig.getKeyPath()));

    // 可以指定是否使用沙箱环境
    payConfig.setUseSandboxEnv(false);

    WxPayService wxPayService = new WxPayServiceImpl();
    wxPayService.setConfig(payConfig);
    return wxPayService;
  }

}
