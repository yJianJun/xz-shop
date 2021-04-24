package com.cdzg.xzshop.config.annotations.api;

import java.lang.annotation.*;

/**
 * 移动应用标注
 * @author Administrator
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MobileApi {
}
