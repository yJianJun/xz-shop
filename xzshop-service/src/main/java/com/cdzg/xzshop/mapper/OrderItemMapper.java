package com.cdzg.xzshop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdzg.xzshop.domain.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单明细表
 * 
 * @author XLQ
 * @email xinliqiang3@163.com
 * @date 2021-05-10 17:12:43
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
	
}
