package com.cdzg.xzshop.mqrecevies;

import com.cdzg.xzshop.config.RabbitMQConfig;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.service.OrderService;
import com.cdzg.xzshop.utils.RabbitmqUtil;
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

import java.util.Date;
import java.util.Objects;

/**
 * @ClassName : AutoSureOrderDelayConsumer
 * @Description : 卖家发货后，买家未主动确认收货，自动收货的监听
 * @Author : EvilPet
 * @Date: 2021-06-28 16:54
 */

@Component
@RabbitListener(queues = RabbitMQConfig.DELAY_AUTO_SURE_ORDER_QUEUE)
@Slf4j
public class AutoSureOrderDelayConsumer implements ChannelAwareMessageListener {


    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitmqUtil rabbitmqUtil;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

    }


    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(String orderId, Message message, Channel channel) throws Exception {
        try {
            //查询订单状态，如果用户未收货，就更改状态为收货
            Order order = orderService.getById(orderId);
            if (Objects.nonNull(order) && order.getOrderStatus() == 3) {
                Date date = new Date();
                order.setOrderStatus(4);
                order.setUpdateBy("system");
                order.setDealTime(date);
                order.setUpdateTime(date);
                orderService.updateById(order);
            }
            //手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("==> 自动确认收货数据处理出现异常，重新发送到队列");
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //消息,重试时间一分钟
            rabbitmqUtil.sendAutoCancelOrderDelayMessage(orderId, 60000);
            //确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
