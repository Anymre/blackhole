package com.mark.web.controller;

import com.mark.web.service.BackService;
import com.mark.service.entity.BackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcus lv
 * @date 2020/8/17 10:58
 */
@RestController
@RequestMapping("/")
public class WebController {
    @Autowired
    private BackService backService;


    @GetMapping
    public BackEntity getOne() {
        return backService.getOne();
    }
}
