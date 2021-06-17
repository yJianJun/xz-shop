package com.cdzg.xzshop.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @ClassName : RabbitMQConfig
 * @Description : rabbitMQ配置文件
 * @Author : EvilPet
 * @Date: 2021-06-16 10:26
 * 注意：如果使用死信功能请分别创建对应的延时交换器与死信交换器，
 * 共用延时交换器与死信交换器列会导致消息消费不及时，因为MQ会默认检测第一个消息的过期时间（队列先进先出）
 * 消息粒度的TTL需要MQ的插件（rabbitmq_delayed_message_exchange）解决，目前暂未使用此插件
 * 使用插件优化后会有局限性问题，设置消息的TTL类型为Integer，最长时间为24天左右，酌情使用
 */
@Configuration
public class RabbitMQConfig {

    ///////////////////////////自动取消订单start////////////////////////////////////

    /**
     * 延时队列，需要定时取消订单的延时消息发往此队列
     * 延时消息需自定义死信队列。并绑定对应路由键
     * 每个死信队列需自定义过期时间，过期后发往各自的配置的死信交换器，并绑定路由键，由死信交换器发往正常消费队列
     */
    public final static String DELAY_AUTO_CANCEL_ORDER_EXCHANGE = "delay_auto_cancel_order_exchange";

    /**
     * 自动取消未支付订单的交换器
     * 死信交换器绑定到死信队列中-绑定CANCEL_ORDER_QUEUE队列
     */
    public final static String AUTO_CANCEL_ORDER_EXCHANGE = "auto_cancel_order_exchange";


    /**
     * 自动取消订单路由键 指定delay_queue 和 auto_cancel_order_queue 的路由规则
     */
    public final static String AUTO_CANCEL_ORDER_KEY = "autoCancelOrder";

    /**
     * 该队列没有直接消费者：而是定义了到达该队列的消息会在一定时间后过期，并在过期后进入到对应的队列中，每个消息都可以自定义自己的过期时间
     */
    public final static String DELAY_AUTO_CANCEL_ORDER_QUEUE = "delay_auto_cancel_order_queue";


    /**
     * 自动取消订单队列（正常消费队列）
     */
    public final static String AUTO_CANCEL_ORDER_QUEUE = "auto_cancel_order_queue";
    ///////////////////////////自动取消订单end//////////////////////////

    ///////////////////////////自动确认收货start//////////////////////////

    /**
     * 商家发货后自动确认收货延时队列
     */
    public final static String DELAY_AUTO_SURE_ORDER_EXCHANGE = "delay_auto_sure_order_exchange";

    /**
     * 商家发货后自动确认收货的交换器
     */
    public final static String AUTO_SURE_ORDER_EXCHANGE = "auto_sure_order_exchange";


    /**
     * 商家发货后自动确认收货路由键
     */
    public final static String AUTO_SURE_ORDER_KEY = "autoSureOrder";

    /**
     * 该队列没有直接消费者：而是定义了到达该队列的消息会在一定时间后过期，并在过期后进入到对应的队列中，每个消息都可以自定义自己的过期时间
     */
    public final static String DELAY_AUTO_SURE_ORDER_QUEUE = "delay_auto_sure_order_queue";


    /**
     * 商家发货后自动确认收货队列（正常消费队列）
     */
    public final static String AUTO_SURE_ORDER_QUEUE = "auto_sure_order_queue";

    ///////////////////////////自动确认收货end//////////////////////////

    //////////////////////绑定关系////////////////////

    /**
     * 处理自动取消订单延时消息交换器
     *
     * @return
     */
    @Bean
    public Exchange delayAutoCancelOrderExchange() {
        return ExchangeBuilder
                .directExchange(DELAY_AUTO_CANCEL_ORDER_EXCHANGE)
                .durable(true)
                .build();
    }


    /**
     * 处理自动取消订单交换器
     *
     * @return
     */
    @Bean
    public Exchange autoCancelOrderExchange() {
        return ExchangeBuilder
                .directExchange(AUTO_CANCEL_ORDER_EXCHANGE)
                .durable(true)
                .build();
    }


    /**
     * 死信队列
     *
     * @return
     */
    @Bean
    public Queue delayAutoCancelOrderQueue() {
        return QueueBuilder.durable(DELAY_AUTO_CANCEL_ORDER_QUEUE)
                // 延迟队列需要设置的消息过期后会发往的交换器名称
                .withArgument("x-dead-letter-exchange", AUTO_CANCEL_ORDER_EXCHANGE)
                // 延迟队列需要设置的消息过期后会发往的路由键名称
                .withArgument("x-dead-letter-routing-key", AUTO_CANCEL_ORDER_KEY)
                .build();
    }


    /**
     * 正常处理订单消息队列， 每个消息过期了都会自动路由到该队列绑定的交换器上
     * 普通队列声明只需要设置队列名称以及是否持久化等信息
     * 此队列有消费监听
     */
    @Bean
    public Queue autoCancelOrderQueue() {
        return QueueBuilder.durable(AUTO_CANCEL_ORDER_QUEUE)
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
     * 正常处理队列绑定到正常处理交换器，并制定路由键为 DELAY_ROUTING_KEY
     * 谁绑定到谁，并指定路由键
     *
     * @return
     */
    @Bean
    public Binding autoCancelOrderBiding() {
        return BindingBuilder.bind(autoCancelOrderQueue())
                .to(autoCancelOrderExchange())
                .with(AUTO_CANCEL_ORDER_KEY).noargs();
    }

    /**
     * 处理自动确认订单延时消息交换器
     *
     * @return
     */
    @Bean
    public Exchange delayAutoSureOrderExchange() {
        return ExchangeBuilder
                .directExchange(DELAY_AUTO_SURE_ORDER_EXCHANGE)
                .durable(true)
                .build();
    }


    /**
     * 处理自动确认订单交换器
     *
     * @return
     */
    @Bean
    public Exchange autoSureOrderExchange() {
        return ExchangeBuilder
                .directExchange(AUTO_SURE_ORDER_EXCHANGE)
                .durable(true)
                .build();
    }


    /**
     * 死信队列
     *
     * @return
     */
    @Bean
    public Queue delayAutoSureOrderQueue() {
        return QueueBuilder.durable(DELAY_AUTO_SURE_ORDER_QUEUE)
                // 延迟队列需要设置的消息过期后会发往的交换器名称
                .withArgument("x-dead-letter-exchange", AUTO_SURE_ORDER_EXCHANGE)
                // 延迟队列需要设置的消息过期后会发往的路由键名称
                .withArgument("x-dead-letter-routing-key", AUTO_SURE_ORDER_KEY)
                .build();
    }


    /**
     * 正常处理自动确认订单消息队列， 每个消息过期了都会自动路由到该队列绑定的交换器上
     * 普通队列声明只需要设置队列名称以及是否持久化等信息
     * 此队列有消费监听
     */
    @Bean
    public Queue autoSureOrderQueue() {
        return QueueBuilder.durable(AUTO_SURE_ORDER_QUEUE)
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
     * 正常处理队列绑定到正常处理交换器，并制定路由键为 DELAY_ROUTING_KEY
     * 谁绑定到谁，并指定路由键
     *
     * @return
     */
    @Bean
    public Binding autoSureOrderBiding() {
        return BindingBuilder.bind(autoSureOrderQueue())
                .to(autoSureOrderExchange())
                .with(AUTO_SURE_ORDER_KEY).noargs();
    }


}
