package com.cdzg.xzshop.mapper;
import org.apache.ibatis.annotations.Param;

import com.cdzg.xzshop.domain.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单表
 * 
 * @author XLQ
 * @email xinliqiang3@163.com
 * @date 2021-05-10 17:12:43
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    Order findById(@Param("id")Long id);

    String findShopIdById(@Param("id")Long id);


	
}
