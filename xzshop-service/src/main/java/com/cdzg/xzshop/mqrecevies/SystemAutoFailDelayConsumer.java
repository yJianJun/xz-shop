package com.cdzg.xzshop.mqrecevies;

import com.cdzg.xzshop.config.RabbitMQConfig;
import com.cdzg.xzshop.service.RefundOrderService;
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

/**
 * @ClassName : AutoCancelOrderDelayConsumer
 * @Description : 卖家同意退货，买家未处理，系统自动失败的监听器
 * @Author : EvilPet
 * @Date: 2021-06-16 13:35
 */

@Component
@RabbitListener(queues = RabbitMQConfig.DELAY_SYSTEM_AUTO_FAIL_QUEUE)
@Slf4j
public class SystemAutoFailDelayConsumer implements ChannelAwareMessageListener {


    @Autowired
    private RabbitmqUtil rabbitmqUtil;

    @Autowired
    private RefundOrderService refundOrderService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

    }


    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(Long refundOrderId, Message message, Channel channel) throws Exception {
        try {
            refundOrderService.systemAutoFail(refundOrderId);
            //手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("==> 卖家同意退货，买家未处理，系统自动失败数据处理出现异常，重新发送到队列");
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //消息,重试时间一分钟
            rabbitmqUtil.sendSystemAutoFailDelayMessage(refundOrderId, 60000);
            //确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }


}
