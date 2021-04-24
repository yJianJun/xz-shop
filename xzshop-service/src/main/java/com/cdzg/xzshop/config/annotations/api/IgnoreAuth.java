package com.cdzg.xzshop.config.annotations.api;


import java.lang.annotation.*;

/**
 * @author sangfor
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {

    /**
     * 取消的地址
     *
     * @return
     */
    String value() default "";
}
