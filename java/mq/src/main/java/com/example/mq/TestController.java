package com.example.mq;


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
    ExtRocketMqTemplate extRocketMqTemplate;
    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @GetMapping
    public String echo() {
        Message<String> helloWorld = MessageBuilder.withPayload("hello world").build();
        TransactionSendResult transactionSendResult = extRocketMqTemplate.sendMessageInTransaction("test_topic", helloWorld, null);
        System.out.println(transactionSendResult.toString());
        try {
            transactionSendResult = rocketMQTemplate.sendMessageInTransaction("test_topic", helloWorld, null);
            System.out.println(transactionSendResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionSendResult.toString();

    }
}
