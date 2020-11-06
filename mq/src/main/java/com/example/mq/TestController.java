package com.example.mq;


import com.example.mq.dto.User;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    RocketMQTemplate rocketmqtemplate;

    @GetMapping
    public String echo() {
        Message<String> helloWorld = MessageBuilder.withPayload("hello world").build();
        TransactionSendResult transactionSendResult = rocketmqtemplate.sendMessageInTransaction("test_topic", helloWorld, null);
        return transactionSendResult.toString();
    }
}
