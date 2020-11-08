package com.example.mq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(consumerGroup = "simpleB", topic = "test_topic", selectorExpression = "B")
@Component
public class SimpleConsumerB implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println(this.getClass().getName() + " rec: " + message);
    }
}
