package com.cdzg.xzshop.config.pay;

import com.cdzg.xzshop.utils.oss.AliyunOssUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import jodd.util.ResourcesUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;

@EqualsAndHashCode(callSuper = true)
@Data
public class WxPayShopConfig extends WxPayConfig {


    @Override
    public SSLContext initSSLContext() throws WxPayException {
        if (StringUtils.isBlank(this.getMchId())) {
            throw new WxPayException("请确保商户号mchId已设置");
        }

        InputStream inputStream;
        if (super.getKeyContent() != null) {
            inputStream = new ByteArrayInputStream(super.getKeyContent());
        } else {
            if (StringUtils.isBlank(this.getKeyPath())) {
                throw new WxPayException("请确保证书文件地址keyPath已配置");
            }
            inputStream = this.loadConfigInputStream(this.getKeyPath());
        }

        try {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            char[] partnerId2charArray = this.getMchId().toCharArray();
            keystore.load(inputStream, partnerId2charArray);
            super.setSslContext(SSLContexts.custom().loadKeyMaterial(keystore, partnerId2charArray).build());
            return super.getSslContext();
        } catch (Exception e) {
            throw new WxPayException("证书文件有问题，请核实！", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 从配置路径 加载配置 信息（支持 classpath、本地路径、网络url）改为 仅支持oss url
     * @param configPath 配置路径
     * @return
     * @throws WxPayException
     */
    private InputStream loadConfigInputStream(String configPath) throws WxPayException {
        try {
            return AliyunOssUtil.downloadFile(configPath);
        } catch (MalformedURLException e) {
            throw new WxPayException(e.getMessage(), e);
        }
    }
}
