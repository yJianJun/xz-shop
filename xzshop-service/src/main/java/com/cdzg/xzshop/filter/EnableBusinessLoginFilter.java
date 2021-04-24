package com.cdzg.xzshop.filter;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * 开启登录拦截, 需要做如下配置:
 * <ul>
 * <li>customer.login.nofilters=xxx 配置不需要登录校验的接口,多个以英文逗号分隔,可选配置</li>
 * </ul>
 * 
 * @version 1.0.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ CorsFilter.class})
public @interface EnableBusinessLoginFilter {

}
