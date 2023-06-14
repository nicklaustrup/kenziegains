package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ExampleRecord;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserRespository extends CrudRepository<ExampleRecord, String> {
}
