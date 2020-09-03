package com.mark.service.repository;

import com.mark.service.entity.TestEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Marcus lv
 * @date 2020/8/13 14:29
 */
@RepositoryRestResource(collectionResourceRel = "test", path = "test")
public interface TestRepository extends CrudRepository<TestEntity, Integer> {
    @Query(value = "select * from s order by score DESC limit 1,2", nativeQuery = true)
    List<TestEntity> findSth();
}
