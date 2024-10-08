package com.cdzg.xzshop.service.Impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.domain.OrderItem;
import com.cdzg.xzshop.mapper.OrderItemMapper;
import com.cdzg.xzshop.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {


    @Override
    public List<OrderItem> getByOrderId(Long orderId) {
        return lambdaQuery().eq(OrderItem::getOrderId, orderId).list();
    }

    @Override
    public List<Long> findIdByOrderIdAndDeleted(Long orderId, Integer deleted) {
        return baseMapper.findIdByOrderIdAndDeleted(orderId, deleted);
    }
}