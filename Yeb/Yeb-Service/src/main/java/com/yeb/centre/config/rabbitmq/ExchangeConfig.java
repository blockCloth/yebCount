package com.yeb.centre.config.rabbitmq;

import com.yeb.centre.pojo.MailContstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dai
 * @create 2022-02-2022/2/10  14-22-54
 */
@Configuration
public class ExchangeConfig {
    //定义交换机
    @Bean
    public DirectExchange directExchange(){
        try {
            return new DirectExchange(MailContstants.EXCHANGE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //创建队列
    @Bean
    public Queue queue(){
        return new Queue(MailContstants.QUEUE_NAME);
    }

    //绑定交换机
    @Bean
    public Binding binding(@Qualifier("queue") Queue queue,
                           @Qualifier("directExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(MailContstants.ROUTING_KEY);
    }
}
