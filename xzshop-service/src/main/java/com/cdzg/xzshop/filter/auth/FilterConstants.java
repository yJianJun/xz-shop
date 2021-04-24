package com.cdzg.xzshop.filter.auth;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Filter常量类
 * 
 * @version 1.0.0
 */
public class FilterConstants {

	/**
	 * 内部忽略地址, 包括swagger, springboot actuator
	 */
	public static final List<String> INTERNEL_IGNORE_URLS = Lists.newArrayList("/autoconfig", "/health", "/info",
			"/configprops", "/mappings", "/metrics", "/swagger-*", "/webjars/*", "/v2/api-docs", "*.js", "*.css",
			"*.html", "*.png", "*.jpg", "*.gif");
}
