package com.example.mq;

import com.example.mq.consumer.SimpleConsumerA;
import com.example.mq.consumer.SimpleConsumerB;
import com.example.mq.consumer.TransacationListener;
import com.example.mq.dto.User;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MqApplicationTests {
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @MockBean
    SimpleConsumerA simpleConsumerA;
    @MockBean
    SimpleConsumerB simpleConsumerB;
    @MockBean
    TransacationListener transacationListener;


    @Test
    void simple() {
        Message<String> message1 = MessageBuilder.withPayload("hello worldA").build();
        Message<String> message2 = MessageBuilder.withPayload("hello worldB").build();
        SendResult sendResult1 = rocketMQTemplate.syncSend("test_topic:A", message1);
        SendResult sendResult2 = rocketMQTemplate.syncSend("test_topic:B", message2);
        System.out.println(sendResult1);
        System.out.println(sendResult2);
    }

    @Test
    void order() {
        for (int i = 0; i < 100; i++) {
            Message<User> message = MessageBuilder.withPayload(new User().setUserAge((byte) i)).build();
            SendResult sendResult = rocketMQTemplate.syncSendOrderly("test_topic:" + (i % 2 == 0 ? "A" : "B"), message, String.valueOf(i % 2));
            System.out.println(sendResult);
        }
    }

    @Test
    void boardCast() {
        Message<User> message = MessageBuilder.withPayload(new User().setUserAge((byte) 10).setUserName("hello world")).build();
        SendResult sendResult = rocketMQTemplate.syncSend("test_topic:C", message);
        System.out.println(sendResult);
    }

    @Test
    void delay() {
        Message<User> message = MessageBuilder.withPayload(new User().setUserAge((byte) 10).setUserName("hello world")).build();
        SendResult sendResult = rocketMQTemplate.syncSend("test_topic:C", message, 1000, 3);
        System.out.println(sendResult);
    }
}
