package com.cdzg.xzshop.service.Impl;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.mapper.OrderMapper;
import com.cdzg.xzshop.service.OrderService;
import org.springframework.stereotype.Service;



@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

	@Override
	public Order findById(Long id){
		 return orderMapper.findById(id);
	}





    @org.springframework.beans.factory.annotation.Autowired
    private OrderMapper orderMapper;
}