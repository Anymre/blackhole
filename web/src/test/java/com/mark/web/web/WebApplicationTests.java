package com.mark.web.web;

import com.mark.service.entity.TestEntity;
import com.mark.service.repository.TestRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class WebApplicationTests {
    @Autowired
    private TestRepository testRepository;

    @Test
    void init() {
        for (int i = 0; i < 100; i++) {
            TestEntity entity = new TestEntity();
            entity.setId(i);
            entity.setScore((int) (Math.random() * 100));
            testRepository.save(entity);
        }
    }

    @Test
    void contextLoads() {
        List<TestEntity> allOrder = testRepository.findSth();
        allOrder.forEach(i-> System.out.println(i.getScore()));
    }

}
