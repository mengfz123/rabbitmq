package com.ivan.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

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
        channel.queueDeclare("ivanQueue", true, false, false, null);
        //发送消息
        String message = "hello world";
        channel.basicPublish("","ivanQueue",null,message.getBytes());
        //释放连接
        channel.close();
        connection.close();
    }
}
