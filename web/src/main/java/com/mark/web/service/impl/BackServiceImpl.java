package com.mark.web.service.impl;

import com.mark.service.entity.BackEntity;
import com.mark.service.repository.BackRepository;
import com.mark.web.service.BackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Marcus lv
 * @date 2020/8/17 11:19
 */
@Service
public class BackServiceImpl implements BackService {
    @Resource
    private BackRepository backRepository;

    @Override
    public BackEntity getOne() {
        return backRepository.findById(1L).get();
    }
}
