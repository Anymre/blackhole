package com.mark.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author Marcus lv
 * @date 2020/8/17 10:58
 */
@RestController
@RequestMapping("/")
@Api(tags = "WebController")
public class WebController {


    @PostMapping
    @ApiOperation(value = "echo")
    public String echo(HttpServletRequest request, @RequestBody String body) {
        return body;
    }
}
