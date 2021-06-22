package com.cdzg.xzshop.mqrecevies;

import com.cdzg.xzshop.config.RabbitMQConfig;
import com.cdzg.xzshop.domain.Order;
import com.cdzg.xzshop.domain.OrderItem;
import com.cdzg.xzshop.service.GoodsSpuService;
import com.cdzg.xzshop.service.OrderItemService;
import com.cdzg.xzshop.service.OrderService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName : AutoCancelOrderDelayConsumer
 * @Description : 买家提交退款，卖家未处理，自动退款的监听器
 * @Author : EvilPet
 * @Date: 2021-06-16 13:35
 */

@Component
@RabbitListener(queues = RabbitMQConfig.DELAY_AUTO_REFUND_QUEUE)
@Slf4j
public class AutoRefundDelayConsumer implements ChannelAwareMessageListener {


    @Autowired
    private RabbitmqUtil rabbitmqUtil;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

    }


    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(String orderId, Message message, Channel channel) throws Exception {
        try {

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
