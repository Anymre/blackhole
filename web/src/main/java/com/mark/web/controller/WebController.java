package com.mark.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Marcus lv
 * @date 2020/8/17 10:58
 */
@RestController
@RequestMapping("/")
@Api(tags = "WebController")
public class WebController {

    @Value("${test:'{a:2}'}")
    private Map<String, Integer> test;


    @GetMapping
    @ApiOperation(value = "echo")
    public Map<String, Integer> echo(@RequestBody String body) {
        return test;
    }

}
