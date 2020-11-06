//package com.example.mq;
//
//import com.example.mq.dto.User;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.stereotype.Service;
//
///**
// * @author mark4z
// */
//@Service
//@RocketMQMessageListener(consumerGroup = "test_cs_2", topic = "test_topic")
//public class TestCSListener implements RocketMQListener<User> {
//
//    @Override
//    public void onMessage(User message) {
//        System.out.println("cs2 re: "+ message.toString());
//    }
//}
