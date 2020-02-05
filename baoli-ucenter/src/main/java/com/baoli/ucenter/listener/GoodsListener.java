package com.baoli.ucenter.listener;

import com.baoli.ucenter.oms.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ys
 * @create 2020-02-04-12:29
 */
@Component
@Slf4j
public class GoodsListener {
    @Autowired
    private OrderService orderService;

    /**
     * 关闭过期订单
     * @param orderId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "close.order.queue", durable = "true"),
            exchange = @Exchange(value = "order.exchange", ignoreDeclarationExceptions = "true"),
            key = {"close.order"}
    ))
    public void closeOrder(String orderId) {
        log.info("自动关闭订单 {}",orderId);
        orderService.closeBackOrder(orderId);

    }

    /**
     * 自动确认收货
     * @param orderId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "confirm.goods.queue", durable = "true"),
            exchange = @Exchange(value = "order.exchange", ignoreDeclarationExceptions = "true"),
            key = {"confirm.goods"}
    ))
    public void confirmGoods(String orderId) {
        log.info("确认收货订单 {}",orderId);
    }


}
