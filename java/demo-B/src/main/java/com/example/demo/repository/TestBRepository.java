package com.example.demo.repository;

import com.example.demo.entity.TestBEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestBRepository extends CrudRepository<TestBEntity, Long> {
}
