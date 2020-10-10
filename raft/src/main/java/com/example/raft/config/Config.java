package com.example.raft.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @author Marcus lv
 * @date 2020/9/27 16:27
 */
@Component
public class Config {
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate build = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(1L))
                .setReadTimeout(Duration.ofSeconds(1L)).build();
        return build;
    }
}
