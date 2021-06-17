package com.cdzg.xzshop.utils;

import com.cdzg.xzshop.config.RabbitMQConfig;
import com.framework.utils.core.ObjectId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName : RabbitmqUtil
 * @Description : rabbitMQ工具类
 * @Author : EvilPet
 * @Date: 2021-06-16 10:26
 */
@Component
@Slf4j
public class RabbitmqUtil {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送自动取消订单的延时消息
     * TTL粒度：每个消息都有自己的TTL
     * @param orderId    订单主键id
     * @param expiration 过期时间 单位毫秒
     */
    public void sendAutoCancelOrderDelayMessage(String orderId, String expiration) {
        // 消息发送时间
        log.info("下单成功后自动取消订单消息发送时间为: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:S")));
        try {
            CorrelationData correlationId = new CorrelationData(String.valueOf(ObjectId.get()));
            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAY_AUTO_CANCEL_ORDER_EXCHANGE, RabbitMQConfig.AUTO_CANCEL_ORDER_KEY, orderId,
                    message -> {
                        message.getMessageProperties().setExpiration(expiration);
                        return message;
                    }, correlationId);
        } catch (AmqpException e) {
            e.printStackTrace();
            log.error("下单成功后自动取消订单消息发送失败，orderId： {}", orderId);
        }
    }

    /**
     * 发送自动确认收货的延时消息
     * TTL粒度：每个消息都有自己的TTL
     * @param orderId    订单主键id
     * @param expiration 过期时间 单位毫秒
     */
    public void sendAutoSureOrderDelayMessage(String orderId, String expiration) {
        // 消息发送时间
        log.info("卖家发货后自动收货消息发送时间为: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:S")));
        try {
            CorrelationData correlationId = new CorrelationData(String.valueOf(ObjectId.get()));
            rabbitTemplate.convertAndSend(RabbitMQConfig.DELAY_AUTO_SURE_ORDER_EXCHANGE, RabbitMQConfig.AUTO_SURE_ORDER_KEY, orderId,
                    message -> {
                        message.getMessageProperties().setExpiration(expiration);
                        return message;
                    }, correlationId);
        } catch (AmqpException e) {
            e.printStackTrace();
            log.error("卖家发货后自动收货消息发送失败，orderId： {}", orderId);
        }
    }

}