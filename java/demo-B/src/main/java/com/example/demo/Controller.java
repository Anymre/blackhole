package com.example.demo;

import com.example.demo.entity.TestBEntity;
import com.example.demo.feign.DemoClient;
import com.example.demo.repository.TestBRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    private DemoClient demoClient;

    @Autowired
    private TestBRepository testBRepository;


    @GetMapping
    @ApiOperation("测试")
    public String getAll() {
        return demoClient.getAll();
    }

    @PostMapping
    @ApiOperation("shift")
    public String shift(@RequestParam int v) {
        Iterable<TestBEntity> all = testBRepository.findAll();
        all.forEach(i -> i.setValue(i.getValue() + v));
        testBRepository.saveAll(all);

        demoClient.incr(-v);

        return demoClient.getAll();
    }

}
