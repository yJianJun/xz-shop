package com.cdzg.xzshop.mqrecevies;

import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.domain.OrderItem;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
import com.cdzg.xzshop.utils.DateUtil;
import com.cdzg.xzshop.utils.RabbitmqUtil;
import com.cdzg.xzshop.vo.order.request.CommitOrderGoodsReqVO;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @ClassName : AutoCancelOrderDelayConsumer
 * @Description : 自动取消订单的监听器
 * @Author : EvilPet
 * @Date: 2021-06-16 13:35
 */

@Component
@RabbitListener(queues = "delay_auto_cancel_order_queue")
@Slf4j
public class AutoCancelOrderDelayConsumer implements ChannelAwareMessageListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private GoodsSpuService goodsSpuService;

    @Autowired
    private RabbitmqUtil rabbitmqUtil;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

    }


    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(String orderId, Message message, Channel channel) throws Exception {
        try {
            //查询订单状态，如果未支付，就更改状态并返库存减销量
            Order order = orderService.getById(orderId);
            if (Objects.nonNull(order) && order.getOrderStatus() == 1) {
                Date date = new Date();
                order.setOrderStatus(5);
                order.setUpdateBy("system");
                order.setUpdateTime(date);
                boolean b = orderService.updateById(order);
                if (b) {
                    //归还库存，减销量
                    List<OrderItem> itemList = orderItemService.getByOrderId(order.getId());
                    if (!CollectionUtils.isEmpty(itemList)) {
                        List<CommitOrderGoodsReqVO> commitGoodsList = new ArrayList<>();
                        itemList.forEach(i -> {
                            CommitOrderGoodsReqVO param = new CommitOrderGoodsReqVO();
                            param.setGoodsId(i.getGoodsId() + "");
                            param.setGoodsCount(-i.getGoodsCount());
                            commitGoodsList.add(param);
                        });
                        goodsSpuService.updateGoodsStockAndSales(commitGoodsList);
                    }
                }
            }
            //手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("==> 自动取消订单数据处理出现异常，重新发送到队列");
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //消息,重试时间一分钟
            rabbitmqUtil.sendAutoCancelOrderDelayMessage(orderId, 60000);
            //确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }


}
