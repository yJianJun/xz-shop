package com.cdzg.xzshop;

import com.cdzg.xzshop.utils.RabbitmqUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName : RabbitMQTest
 * @Description : rabbitmq
 * @Author : EvilPet
 * @Date: 2021-06-17 09:34
 */
public class RabbitMQTest extends BaseTest {

    @Autowired
    RabbitmqUtil rabbitmqUtil;


    @Test
    void sendMQTest(){
        rabbitmqUtil.sendAutoCancelOrderDelayMessage("测试30秒",30000);
    }

    @Test
    void sendMQTest1(){
        rabbitmqUtil.sendAutoCancelOrderDelayMessage("测试10秒",10000);
    }
}
