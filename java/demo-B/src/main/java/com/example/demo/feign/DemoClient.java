package com.example.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "demo", path = "/")
public interface DemoClient {
    @RequestMapping(method = RequestMethod.GET)
    String getAll();

    @RequestMapping(method = RequestMethod.POST)
    void incr(@RequestParam int v);
}
