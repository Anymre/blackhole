package com.example.raft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcus lv
 * @date 2020/9/27 16:27
 */
@Component
public class Config {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
