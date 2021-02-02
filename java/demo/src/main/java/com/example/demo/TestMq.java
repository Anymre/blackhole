package com.example.demo;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Marcus lv
 * @date 2021/1/12 17:17
 */
public class TestMq {

    @StreamListener(Sink.INPUT)
    public void onMessage(String message) {
        System.out.println(message);
    }
}
