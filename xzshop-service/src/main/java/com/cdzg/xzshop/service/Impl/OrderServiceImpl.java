package com.cdzg.xzshop.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.domain.OrderItem;
import com.cdzg.xzshop.mapper.OrderItemMapper;
import com.cdzg.xzshop.mapper.OrderMapper;
import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
import com.cdzg.xzshop.vo.order.request.CommitOrderGoodsReqVO;
import com.cdzg.xzshop.vo.order.request.CommitOrderReqVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderItemService orderItemService;


    @Override
    public Order findById(Long id) {
        return orderMapper.findById(id);
    }

    @Override
    public String findShopIdById(Long id) {
        return orderMapper.findShopIdById(id);
    }

    /**
     * app用户提交商城订单
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public Order commitOrder(CommitOrderReqVO request) {
        List<CommitOrderGoodsReqVO> goodsList = request.getGoodsList();
        Date date = new Date();
        //保存订单信息及订单商品信息
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        BeanUtils.copyProperties(request, order);
        order.setFreight(request.getFare());
        order.setCreateTime(date);
        if (request.getOrderType() == 1) {
            //积分订单
            order.setPayMethod(3);
            order.setPayMoney(request.getTotalMoney());
            order.setPayTime(date);
            order.setOrderStatus(2);
        } else {
            order.setOrderStatus(0);
        }
        int i = baseMapper.insert(order);
        boolean save = false;
        if (i > 0) {
            goodsList.forEach(g -> {
                OrderItem orderItem = new OrderItem();
                BeanUtils.copyProperties(g, orderItem);
                orderItem.setOrderId(order.getId());
                orderItem.setGoodsId(Long.valueOf(g.getGoodsId()));
                orderItem.setStatus(0);
                orderItem.setGoodsUnitPrice(g.getPromotionPrice());
                orderItems.add(orderItem);
            });
            save = orderItemService.saveBatch(orderItems);
        }
        if (i > 0 && save) {
            return order;
        }
        return null;
    }


}