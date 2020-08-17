package com.mark.service;

import com.mark.service.entity.BackEntity;
import com.mark.service.repository.BackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Marcus lv
 * @date 2020/8/17 11:19
 */
@Service
public class BackServiceImpl implements BackService {
    @Autowired
    private BackRepository backRepository;

    @Override
    public BackEntity getOne() {
        return backRepository.findById(1L).get();
    }
}
