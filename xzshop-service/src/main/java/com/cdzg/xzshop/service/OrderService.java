package com.cdzg.xzshop.service;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.Order;

import java.util.Map;

/**
 * 订单表
 *
 * @author XLQ
 * @email xinliqiang3@163.com
 * @date 2021-05-10 17:12:43
 */
public interface OrderService extends IService<Order> {



	Order findById(Long id);



	String findShopIdById(Long id);



}

