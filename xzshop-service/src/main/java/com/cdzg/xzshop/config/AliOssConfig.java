package com.cdzg.xzshop.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliOssProperties.class)
public class AliOssConfig {

	@Autowired
	private AliOssProperties AliOssProperties;
	
	@Bean
	public OSSClient ossClient() {
		return new OSSClient(AliOssProperties.getEndPoint(), AliOssProperties.getAccessKeyId(), AliOssProperties.getAccessKeySecret());
	}

}
