package com.mark.service.repository;

import com.mark.service.entity.BackEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Marcus lv
 * @date 2020/8/13 14:29
 */
@Repository
public interface BackRepository extends CrudRepository<BackEntity, Long> {

}
