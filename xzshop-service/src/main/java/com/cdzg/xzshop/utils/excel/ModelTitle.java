/**************************************************************	
* Copyright©2017-2017 by hentica All Rights Reserved	
* 创建日期：2017年10月31日 下午8:01:14
* 作       者：bbliu
* 功能描述：
**************************************************************/

package com.cdzg.xzshop.utils.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author bbliu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModelTitle {
	public String name();
}
