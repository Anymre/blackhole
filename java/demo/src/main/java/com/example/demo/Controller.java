package com.example.demo;

import com.example.demo.entity.TestEntity;
import com.example.demo.repository.TestRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    private TestRepository testRepository;

    @GetMapping
    @ApiOperation("获取所有")
    public Iterable<TestEntity> getAll() {
        return testRepository.findAll();
    }

    @PostMapping
    @ApiOperation("incr")
    public void post(@RequestParam int v) {
        Iterable<TestEntity> all = testRepository.findAll();
        all.forEach(i -> i.setValue(i.getValue() + v));
        testRepository.saveAll(all);
    }
}
