package com.mark.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
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
    
    
    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
    @ApiOperation(value = "echo")
    public XmlRequest echo(HttpServletRequest request, @RequestBody String body) {
        XmlRequest request1 = new XmlRequest();
        request1.setAgentId("1");
        return request1;
    }
    
}
