package com.ivan.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    //设置confirm和return回调
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            if (ack) {
                System.out.println("消息发送成功到交换机成功！");
            } else {
                System.out.println("消息发送失败，原因是："+cause);
            }
        });
       rabbitTemplate.setReturnsCallback((returnedMessage)->{
           System.out.println("消息投递到队列失败，原因为："+returnedMessage.getReplyText());
       });

        return rabbitTemplate;
    }

    public static final String exchangeName = "bootExchange";
    public static final String queueName = "bootQueue";
    //定义交换机
    @Bean
    public DirectExchange bootExchange() {
        return ExchangeBuilder.directExchange(exchangeName).durable(true).build();
    }

    //定义死信交换机
    @Bean
    public DirectExchange deadExchange() {
        DirectExchange deadExchange = ExchangeBuilder.directExchange("deadExchange").durable(true).build();

        return deadExchange;
    }



    //定义队列
    @Bean
    public Queue bootQueue() {

        //设置消息10秒未被消费自动从队列中删除
        Queue build = QueueBuilder.durable(queueName).build();
        build.addArgument("x-message-ttl",10000);
        //绑定死信交换机
        build.addArgument("x-dead-letter-exchange","deadExchange");
        //设置死信队列路由key
        build.addArgument("x-dead-letter-routing-key","deadKey");

        return build;
    }
    //定义死信队列
    @Bean
    public Queue deadQueue() {
        //设置消息10秒未被消费自动从队列中删除
        Queue build = QueueBuilder.durable("deadQueue").build();
        return build;
    }

    //死信队列绑定死信交换机
    @Bean
    public Binding bindingDeadQueueAndDeadExchange(@Qualifier("deadQueue") Queue queue,@Qualifier("deadExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("deadKey");
    }


    //定义交换机和队列的绑定关系
    @Bean
    public Binding bindingQueueAndExchange(@Qualifier(queueName) Queue queue,@Qualifier(exchangeName) DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("testKey");
    }

}
