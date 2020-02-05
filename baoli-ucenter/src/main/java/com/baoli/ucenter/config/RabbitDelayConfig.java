package com.baoli.ucenter.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ys
 * @create 2020-02-04-18:46
 */
@Configuration
public class RabbitDelayConfig {
    @Bean
    public Exchange delayExchange() {
        return new DirectExchange("order.delay.exchange", true, false);
    }

    @Bean
    public Queue delayCloseOrderQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 60 * 1000 * 5);//这个队列里面所有消息的过期时间
        arguments.put("x-dead-letter-exchange", "order.exchange");//消息死了交给那个交换机
        arguments.put("x-dead-letter-routing-key", "close.order");//死信发出去的路由键
        return new Queue("close.order.delay.queue", true,
                false,
                false,
                arguments);
    }

    @Bean
    public Queue delayConfirmGoodsQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 1000 * 60 * 60 * 6);//这个队列里面所有消息的过期时间
        arguments.put("x-dead-letter-exchange", "order.exchange");//消息死了交给那个交换机
        arguments.put("x-dead-letter-routing-key", "confirm.goods");//死信发出去的路由键
        return new Queue("confirm.goods.delay.queue", true,
                false,
                false,
                arguments);
    }

    @Bean
    public Binding delayOrderBinding() {
        return new Binding("close.order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order.delay.exchange",
                "order.delay", null);
    }
    @Bean
    public Binding delayGoodsBinding() {
        return new Binding("confirm.goods.delay.queue",
                Binding.DestinationType.QUEUE,
                "order.delay.exchange",
                "goods.delay", null);
    }
}
