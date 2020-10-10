package com.mark.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcus lv
 * @date 2020/8/17 10:58
 */
@RestController
@RequestMapping("/")
@Api(tags = "WebController")
public class WebController {
    
    @GetMapping
    @ApiOperation("echo")
    public String echo(@RequestParam String echo) {
        return echo;
    }
}
