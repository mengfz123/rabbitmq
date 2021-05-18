package com.ivan.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RabbitListener(queues = "bootQueue")
public class rabbitListener {


    @RabbitHandler
    public void handle(String str, Channel channel, Message message) throws Exception {

        Thread.sleep(1000);
        System.out.println("接受到消息:"+str);
        System.out.println("正在处理业务逻辑");
        try {
            //int i = 1/0;
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println("消息处理完毕");
        } catch (Exception e) {
            System.out.println("消息处理失败");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }

}
