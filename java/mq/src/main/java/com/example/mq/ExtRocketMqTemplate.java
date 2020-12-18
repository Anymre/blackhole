package com.example.mq;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

@ExtRocketMQTemplateConfiguration(group = "trans")
@Component
public class ExtRocketMqTemplate extends RocketMQTemplate {
}
