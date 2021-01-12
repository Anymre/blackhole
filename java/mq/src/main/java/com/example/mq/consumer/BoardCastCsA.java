package com.example.mq.consumer;

import com.example.mq.dto.User;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(consumerGroup = "BoardCastCs", topic = "test_topic", selectorExpression = "C", messageModel = MessageModel.BROADCASTING)
@Component
public class BoardCastCsA implements RocketMQListener<User> {

    @Override
    public void onMessage(User message) {
        System.out.println(this.getClass().getName() + " rec: " + message);
    }

}
