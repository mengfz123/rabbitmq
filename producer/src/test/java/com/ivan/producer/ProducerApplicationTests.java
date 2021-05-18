package com.ivan.producer;

import com.ivan.producer.config.RabbitmqConfig;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
class ProducerApplicationTests {


    @Autowired
    private RabbitTemplate template;
    @Test
    void contextLoads() throws InterruptedException {

        for (int i = 0; i < 100; i++) {

            template.convertAndSend(RabbitmqConfig.exchangeName,"testKey","this is my first message");
        }
    }

}
