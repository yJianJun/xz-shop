/**************************************************************	
* Copyright©2017-2017 by hentica All Rights Reserved	
* 创建日期：2017年9月4日 下午3:49:42
* 作       者：bbliu
* 功能描述：
**************************************************************/

package com.cdzg.xzshop.utils.excel;

import java.lang.annotation.*;

/** 注解会在class字节码文件中存在，在运行时可以通过反射获取到 */
@Retention(RetentionPolicy.RUNTIME)
/** 定义注解的作用目标**作用范围字段、枚举的常量/方法 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD })
/** 说明该注解将被包含在javadoc中 */
@Documented
public @interface ExcelFieldMeta {

	/**
	 * 注释
	 * 
	 * @return
	 */
	String explain() default "";

	/**
	 * 测试值
	 * 
	 * @return
	 */
	String testVal() default "";

	/**
	 * Excel表列
	 * 
	 * @return
	 */
	int colIndex() default -1;

	/**
	 * 是否允许空
	 * 
	 * @return
	 */
	boolean nullable() default true;

	/**
	 * 
	 * @return
	 */
	String interfaceXmlName() default "";
}
