package com.cdzg.xzshop.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName : RabbitMQConfig
 * @Description : rabbitMQ配置文件
 * @Author : EvilPet
 * @Date: 2021-06-16 10:26
 * 注意：如果使用死信功能请分别创建对应的延时交换器与死信交换器，
 * 共用延时交换器与死信交换器列会导致消息消费不及时，因为MQ会默认检测第一个消息的过期时间（队列先进先出）
 * 消息粒度的TTL需要MQ的插件（rabbitmq_delayed_message_exchange）解决
 * 使用插件优化后会有局限性问题，设置消息的TTL类型为Integer，最长时间为24天左右，酌情使用
 */
@Configuration
public class RabbitMQConfig {

    ///////////////////////////自动取消订单start////////////////////////////////////

    /**
     * 延时队列，需要定时取消订单的延时消息发往此队列，每个消息有自己的TTL
     */
    public final static String DELAY_AUTO_CANCEL_ORDER_EXCHANGE = "delay_auto_cancel_order_exchange";


    /**
     * 自动取消订单路由键 指定delay_queue 和 auto_cancel_order_queue 的路由规则
     */
    public final static String AUTO_CANCEL_ORDER_KEY = "autoCancelOrder";

    /**
     * 该延时队列具有消费者：定义了到达该队列的消息会在一定时间后过期，并在过期后被消费，每个消息都可以自定义自己的过期时间
     */
    public final static String DELAY_AUTO_CANCEL_ORDER_QUEUE = "delay_auto_cancel_order_queue";

    ///////////////////////////自动取消订单end//////////////////////////

    ///////////////////////////自动确认收货start//////////////////////////

    /**
     * 商家发货后自动确认收货延时队列
     */
    public final static String DELAY_AUTO_SURE_ORDER_EXCHANGE = "delay_auto_sure_order_exchange";

    /**
     * 商家发货后自动确认收货路由键
     */
    public final static String AUTO_SURE_ORDER_KEY = "autoSureOrder";

    /**
     * 到达该队列的消息会在一定时间后过期，并在过期后被消费，每个消息都可以自定义自己的过期时间
     */
    public final static String DELAY_AUTO_SURE_ORDER_QUEUE = "delay_auto_sure_order_queue";

    /**
     * 申请退款商家未处理延时队列
     */
    public final static String DELAY_AUTO_REFUND_EXCHANGE = "delay_auto_refund_exchange";

    /**
     * 申请退款商家未处理路由键
     */
    public final static String AUTO_REFUND_KEY = "auto_refund";

    /**
     * 到达该队列的消息会在一定时间后过期，并在过期后被消费，每个消息都可以自定义自己的过期时间
     */
    public final static String DELAY_AUTO_REFUND_QUEUE = "delay_auto_refund_queue";

    /**
     * 买家提交退货退款申请，卖家未处理延时队列
     */
    public final static String DELAY_SYSTEM_AUTO_DEAL_EXCHANGE = "delay_system_auto_deal_exchange";

    /**
     * 买家提交退货退款申请，卖家未处理路由键
     */
    public final static String SYSTEM_AUTO_DEAL_KEY = "system_auto_deal";

    /**
     * 到达该队列的消息会在一定时间后过期，并在过期后被消费，每个消息都可以自定义自己的过期时间
     */
    public final static String DELAY_SYSTEM_AUTO_DEAL_QUEUE = "delay_system_auto_deal_queue";

    /**
     * 卖家同意退货，买家未处理延时队列
     */
    public final static String DELAY_SYSTEM_AUTO_FAIL_EXCHANGE = "delay_system_auto_fail_exchange";

    /**
     * 卖家同意退货，买家未处理路由键
     */
    public final static String SYSTEM_AUTO_FAIL_KEY = "system_auto_fail";

    /**
     * 到达该队列的消息会在一定时间后过期，并在过期后被消费，每个消息都可以自定义自己的过期时间
     */
    public final static String DELAY_SYSTEM_AUTO_FAIL_QUEUE = "delay_system_auto_fail_queue";

    /**
     * 卖家确认收货，未处理退款延时队列
     */
    public final static String DELAY_SYSTEM_AUTO_REFUND_EXCHANGE = "delay_system_auto_refund_exchange";

    /**
     * 卖家确认收货，未处理退款路由键
     */
    public final static String SYSTEM_AUTO_REFUND_KEY = "system_auto_refund";

    /**
     * 到达该队列的消息会在一定时间后过期，并在过期后被消费，每个消息都可以自定义自己的过期时间
     */
    public final static String DELAY_SYSTEM_AUTO_REFUND_QUEUE = "delay_system_auto_refund_queue";

    ///////////////////////////自动确认收货end//////////////////////////

    //////////////////////绑定关系////////////////////

    /**
     * 处理自动取消订单延时消息交换器
     *
     * @return
     */
    @Bean
    public Exchange delayAutoCancelOrderExchange() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_AUTO_CANCEL_ORDER_EXCHANGE, "x-delayed-message", true, false, args);
    }


    /**
     * 处理自动取消订单的延时消息队列
     *
     * @return
     */
    @Bean
    public Queue delayAutoCancelOrderQueue() {
        return QueueBuilder.durable(DELAY_AUTO_CANCEL_ORDER_QUEUE)
                .build();
    }

    /**
     * 延时交换器和队列的绑定关系
     *
     * @return
     */
    @Bean
    public Binding delayAutoCancelOrderBiding() {
        return BindingBuilder.bind(delayAutoCancelOrderQueue())
                .to(delayAutoCancelOrderExchange())
                .with(AUTO_CANCEL_ORDER_KEY).noargs();
    }


    /**
     * 处理自动确认订单延时消息交换器
     *
     * @return
     */
    @Bean
    public Exchange delayAutoSureOrderExchange() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_AUTO_SURE_ORDER_EXCHANGE, "x-delayed-message", true, false, args);
    }


    /**
     * 延时队列
     *
     * @return
     */
    @Bean
    public Queue delayAutoSureOrderQueue() {
        return QueueBuilder.durable(DELAY_AUTO_SURE_ORDER_QUEUE)
                .build();
    }


    /**
     * 延时交换器和队列的绑定关系
     *
     * @return
     */
    @Bean
    public Binding delayAutoSureOrderBiding() {
        return BindingBuilder.bind(delayAutoSureOrderQueue())
                .to(delayAutoSureOrderExchange())
                .with(AUTO_SURE_ORDER_KEY).noargs();
    }


    /**
     * 买家提交退款，卖家未处理，自动退款延时消息交换器
     *
     * @return
     */
    @Bean
    public Exchange delayAutoRefundExchange() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_AUTO_REFUND_EXCHANGE, "x-delayed-message", true, false, args);
    }


    /**
     * 延时队列
     *
     * @return
     */
    @Bean
    public Queue delayAutoRefundQueue() {
        return QueueBuilder.durable(DELAY_AUTO_REFUND_QUEUE)
                .build();
    }


    /**
     * 延时交换器和队列的绑定关系
     *
     * @return
     */
    @Bean
    public Binding delayAutoRefundBiding() {
        return BindingBuilder.bind(delayAutoRefundQueue())
                .to(delayAutoRefundExchange())
                .with(AUTO_REFUND_KEY).noargs();
    }


    /**
     * 买家提交退货退款申请，卖家未处理延时消息交换器
     *
     * @return
     */
    @Bean
    public Exchange delaySystemAutoDealExchange() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_SYSTEM_AUTO_DEAL_EXCHANGE, "x-delayed-message", true, false, args);
    }


    /**
     * 延时队列
     *
     * @return
     */
    @Bean
    public Queue delaySystemAutoDealQueue() {
        return QueueBuilder.durable(DELAY_SYSTEM_AUTO_DEAL_QUEUE)
                .build();
    }


    /**
     * 延时交换器和队列的绑定关系
     *
     * @return
     */
    @Bean
    public Binding delaySystemAutoDealBiding() {
        return BindingBuilder.bind(delaySystemAutoDealQueue())
                .to(delaySystemAutoDealExchange())
                .with(SYSTEM_AUTO_DEAL_KEY).noargs();
    }


    /**
     * 卖家同意退货，买家未处理延时消息交换器
     *
     * @return
     */
    @Bean
    public Exchange delaySystemAutoFailExchange() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_SYSTEM_AUTO_FAIL_EXCHANGE, "x-delayed-message", true, false, args);
    }


    /**
     * 延时队列
     *
     * @return
     */
    @Bean
    public Queue delaySystemAutoFailQueue() {
        return QueueBuilder.durable(DELAY_SYSTEM_AUTO_FAIL_QUEUE)
                .build();
    }


    /**
     * 延时交换器和队列的绑定关系
     *
     * @return
     */
    @Bean
    public Binding delaySystemAutoFailBiding() {
        return BindingBuilder.bind(delaySystemAutoDealQueue())
                .to(delaySystemAutoDealExchange())
                .with(SYSTEM_AUTO_FAIL_KEY).noargs();
    }


    /**
     * 卖家同意退货，买家未处理延时消息交换器
     *
     * @return
     */
    @Bean
    public Exchange delaySystemAutoRefundExchange() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_SYSTEM_AUTO_REFUND_EXCHANGE, "x-delayed-message", true, false, args);
    }


    /**
     * 延时队列
     *
     * @return
     */
    @Bean
    public Queue delaySystemAutoRefundQueue() {
        return QueueBuilder.durable(DELAY_SYSTEM_AUTO_REFUND_QUEUE)
                .build();
    }


    /**
     * 延时交换器和队列的绑定关系
     *
     * @return
     */
    @Bean
    public Binding delaySystemAutoRefundBiding() {
        return BindingBuilder.bind(delaySystemAutoDealQueue())
                .to(delaySystemAutoDealExchange())
                .with(SYSTEM_AUTO_REFUND_KEY).noargs();
    }

}
