package com.baoli.ucenter.test;

import com.baoli.common.constans.UCenterContant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ys
 * @create 2020-02-04-11:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UCenterContant.class)
public class RabbitMqTest {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Test
    public void sendMsg(){
        String msg = "hello, Spring boot amqp";
        this.amqpTemplate.convertAndSend("spring.test.exchange","a.b", msg);
    }
}
