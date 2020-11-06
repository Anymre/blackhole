package com.example.mq;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;

@SpringBootTest
public class MqApplicationTests {
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    RestTemplate restTemplate = new RestTemplate();
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjb25uZXh0LWVlcyIsImlhdCI6MTYwNDU2Nzk2MjU3OSwiZXhwIjoxNjA0NjU0MzYyNTc5LCJhdWRPcmdhbml6YXRpb24iOiJBbXdheSIsImF1ZFByb2plY3QiOiJoeWJyaXMiLCJhdWRTZXJ2aWNlIjoiQWNjbCIsImF1ZEVudmlyb25tZW50IjoiRnQxIn0.nZXdy84T2pA7rVz-XDPOd8EIjCSfJ-MEvDqSBP_PD2w";
    String url = "http://10.140.221.100:80/amway-commonService/short-url/create";

    String testUrl = "https://mall.amway.com.cn/decrypt/link/g1kzH7Plncsg6jG02PveMg==/g1kzH7Plncsg6jG02PveMg==/iKoL0gIi1hGdjk5SwhuIfqLgcuxej_HWG9RyadFTn78=";

    @Test
    void sql() {
        String sql = "UPDATE `t_user_account_bill#{i}`\n" +
                "set `is_deleted` = 1, `update_time` = now(), `update_user` = '2020-11-05 1129'\n" +
                "WHERE `activity_id` = 1310500758289928193\n" +
                "  and `is_deleted` = 0\n" +
                "  and `channel` not in (106,107)\n" +
                "and `create_time` < '2020-11-06 00:00:00';";
        for (int i = 0; i < 10; i++) {
            System.out.println(sql.replace("#{i}", "" + i));
            System.out.println();
        }
    }

    @Test
    void con() {
        for (int i = 0; i < 10; i++) {
            try {
                sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void sync() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        String testUrlCopy = "https://mall.amway.com.cn/decrypt/link/g1kzH7Plncsg6jG02PveMg==/g1kzH7Plncsg6jG02PveMg==/iKoL0gIi1hGdjk5SwhuIfqLgcuxej_HWG9RyadFTn78=";
        testUrlCopy = testUrlCopy + 1234234710;
        HttpEntity httpEntity = test(testUrlCopy);


        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                ResponseEntity<String> exchange = restTemplate
                        .exchange(this.url, HttpMethod.POST, httpEntity, String.class);

                String body = exchange.getBody();
                System.out.println(body);
            });
        }
        Thread.sleep(10000L);
    }

    @Test
    void test2() {
        HttpHeaders header = new HttpHeaders();
        header.add("token", token);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, header);


        ResponseEntity<String> exchange = restTemplate
                .exchange("http://10.140.221.100:80/amway-commonService/short-url/cleanAll?key=zzX44Xmj", HttpMethod.DELETE, httpEntity, String.class);
        System.out.println(exchange.getBody());
    }

    @Test
    HttpEntity test(String url) {
        HttpHeaders header = new HttpHeaders();
        header.add("token", token);
        header.add("Content-Type", "application/json");
        HttpEntity httpEntity = new HttpEntity<>("{\"realUrl\":\"" + url + "\"}", header);
        return httpEntity;
    }

    static class Result {
        long timeStamp;
        int status;
        String results;
        int errorCode;
        String errorMessage;

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getResults() {
            return results;
        }

        public void setResults(String results) {
            this.results = results;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    @Test
    void contextLoads() throws URISyntaxException {

        String sendTime = String.valueOf(System.currentTimeMillis());

        Map<String, String> params = new LinkedHashMap<>();
        params.put("audOrganization", "Amway");
        params.put("audProject", "hybris");
        params.put("audService", "Accl");
        params.put("audEnvironment", "Ft1");
        params.put("sendTime", sendTime);

        HttpHeaders header = new HttpHeaders();
        URI url = HttpUtil.buildUri("http://10.140.221.100:80/gateway/jwt",
                Query.newInstance().initParams(params));

        header.add("sdata", CommonServiceUtil.generateSign("MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCA7/8pQGPoi9QYuPijZYaCpeHRgqy8FizMGpqaaJB5prg==", url.getQuery()));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(null, header);

        ResponseEntity<String> exchange = restTemplate
                .exchange(url.toString(), HttpMethod.GET, httpEntity, String.class);
        System.out.println(exchange.getBody());
    }
}
