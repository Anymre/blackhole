package com.example.demo;

import com.example.demo.entity.TestBEntity;
import com.example.demo.feign.DemoClient;
import com.example.demo.repository.TestBRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {
    @Autowired
    private DemoClient demoClient;

    @Autowired
    private TestBRepository testBRepository;

    private ObjectMapper objectMapper = new ObjectMapper();


    @GetMapping
    @ApiOperation("测试")
    public Object getAll() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>(8);
        map.put("B", testBRepository.findAll());
        map.put("A", demoClient.getAll());
        return map;
    }

    @PostMapping
    @ApiOperation("shift")
    @Transactional(rollbackFor = Exception.class)
    public String shift(@RequestParam int v) throws Exception {
        demoClient.incr(-v);


        Iterable<TestBEntity> all = testBRepository.findAll();
        all.forEach(i -> i.setValue(i.getValue() + v));
        testBRepository.saveAll(all);

        if (v % 2 == 1) {
            throw new Exception("" + v);
        }

        return demoClient.getAll();
    }

}
