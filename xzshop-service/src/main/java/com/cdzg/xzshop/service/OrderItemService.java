package com.cdzg.xzshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdzg.xzshop.domain.OrderItem;

import java.util.List;


/**
 * 订单明细表
 *
 * @author XLQ
 * @email xinliqiang3@163.com
 * @date 2021-05-10 17:12:43
 */
public interface OrderItemService extends IService<OrderItem> {
    /**
     * 根据订单id获取所有订单明细
     * @param orderId
     * @return
     */
    List<OrderItem> getByOrderId(Long orderId);

    List<Long> findIdByOrderIdAndDeleted(Long orderId, Integer deleted);
}

