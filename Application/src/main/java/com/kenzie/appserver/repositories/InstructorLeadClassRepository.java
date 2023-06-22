package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.InstructorLeadClassRecord;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface InstructorLeadClassRepository extends CrudRepository<InstructorLeadClassRecord, String> {
}
