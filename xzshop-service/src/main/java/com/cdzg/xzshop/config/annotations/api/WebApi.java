package com.cdzg.xzshop.config.annotations.api;

import java.lang.annotation.*;

/**
 * web接口标注
 * @author Administrator
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebApi {
}
