package com.ivan.consumer;

import com.rabbitmq.client.*;
import org.springframework.messaging.simp.config.ChannelRegistration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建mq的工厂
        ConnectionFactory factory = new ConnectionFactory();

        //设置工厂相关参数
        factory.setHost("192.168.109.128");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/");
        //获取连接
        Connection connection = factory.newConnection();
        //获取channel
        Channel channel = connection.createChannel();
        //创建队列
        //channel.queueDeclare("ivanQueue", true, false, false, null);

        Consumer consumer = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("consumerTag:"+ consumerTag);
                System.out.println("envelope:"+ envelope);
                System.out.println("properties:"+ properties);
                System.out.println("body:"+ new String(body));

            }
        };

        //接收消息
        channel.basicConsume("ivanQueue",true,consumer);

    }
}
