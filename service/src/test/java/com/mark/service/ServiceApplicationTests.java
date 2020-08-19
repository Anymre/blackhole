package com.mark.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServiceApplicationTests {

    @Test
    void contextLoads() {
        String s = "INSERT INTO `extra-server`.t_survey " +
                "(id, title, type, questionnaire_id, ada, publish, uses, create_user, create_time, update_user, update_time, status, is_deleted) VALUES " +
                "(#{id}, 'Survey-#{id}', 1, 1, '123456', 1, 30, '12345', '2020-08-14 10:16:24', '', '2020-08-14 10:16:24', 1, 0);\n";
        for (int i = 2; i < 30; i++) {
            System.out.println(s.replace("#{id}", i+""));
        }
    }

}
