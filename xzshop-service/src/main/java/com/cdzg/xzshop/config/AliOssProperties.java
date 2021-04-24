package com.cdzg.xzshop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="ali.oss")
public class AliOssProperties {

	private String endPoint;
	private String accessKeyId;
	private String accessKeySecret;
	
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	@Override
	public String toString() {
		return "AliOssProperties [endPoint=" + endPoint + ", accessKeyId=" + accessKeyId + ", accessKeySecret="
				+ accessKeySecret + "]";
	}
	
}
