package com.kenzie.capstone.service.dao;

import com.kenzie.capstone.service.model.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.common.collect.ImmutableMap;

import java.time.LocalDateTime;
import java.util.List;

public class ExampleDao {
    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Match objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public ExampleDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public ExampleData storeExampleData(ExampleData exampleData) {
        try {
            mapper.save(exampleData, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id has already been used");
        }

        return exampleData;
    }

    public List<ExampleRecord> getExampleData(String id) {
        ExampleRecord exampleRecord = new ExampleRecord();
        exampleRecord.setId(id);

        DynamoDBQueryExpression<ExampleRecord> queryExpression = new DynamoDBQueryExpression<ExampleRecord>()
                .withHashKeyValues(exampleRecord)
                .withConsistentRead(false);

        return mapper.query(ExampleRecord.class, queryExpression);
    }

    public ExampleRecord setExampleData(String id, String data) {
        ExampleRecord exampleRecord = new ExampleRecord();
        exampleRecord.setId(id);
        exampleRecord.setData(data);

        try {
            mapper.save(exampleRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "id",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("id already exists");
        }

        return exampleRecord;
    }

    //Set additional methods for each of the classes/tables
    public List<InstructorLeadClassRecord> getInstructorLeadClassData(String classId) {
        InstructorLeadClassRecord instructorLeadClassRecord = new InstructorLeadClassRecord();
        instructorLeadClassRecord.setClassId(classId);

        DynamoDBQueryExpression<InstructorLeadClassRecord> queryExpression = new DynamoDBQueryExpression<InstructorLeadClassRecord>()
                .withHashKeyValues(instructorLeadClassRecord)
                .withConsistentRead(false);

        return mapper.query(InstructorLeadClassRecord.class, queryExpression);
    }

    public InstructorLeadClassRecord setInstructorLeadClassData(String classId, String name, String description, String classType, String userId, int classCapacity, LocalDateTime dateTime, boolean status) {
        InstructorLeadClassRecord instructorLeadClassRecord = new InstructorLeadClassRecord();
        instructorLeadClassRecord.setClassId(classId);
        instructorLeadClassRecord.setName(name);
        instructorLeadClassRecord.setDescription(description);
        instructorLeadClassRecord.setClassType(classType);
        instructorLeadClassRecord.setUserId(userId);
        instructorLeadClassRecord.setClassCapacity(classCapacity);
        instructorLeadClassRecord.setDateTime(dateTime);
        instructorLeadClassRecord.setStatus(status);

        try {
            mapper.save(instructorLeadClassRecord, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "classId",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("classId already exists");
        }

        return instructorLeadClassRecord;
    }


    //User Tables
    public List<UserRecord> getUserData(String username) {
        UserRecord record = new UserRecord();
        record.setUsername(username);

        DynamoDBQueryExpression<UserRecord> queryExpression = new DynamoDBQueryExpression<UserRecord>()
                .withHashKeyValues(record)
                .withConsistentRead(false);

        return mapper.query(UserRecord.class, queryExpression);
    }

    public UserRecord setUserData(UserData data) {
        UserRecord record = new UserRecord();
        record.setUserId(data.getUserId());
        record.setUsername(data.getUsername());
        record.setFirstName(data.getFirstName());
        record.setLastName(data.getLastName());
        record.setMembership(data.getMembership());
        record.setStatus(data.getStatus());
        record.setUserType(record.getUserType());
        record.setPassword(record.getPassword());


        try {
            mapper.save(record, new DynamoDBSaveExpression()
                    .withExpected(ImmutableMap.of(
                            "username",
                            new ExpectedAttributeValue().withExists(false)
                    )));
        } catch (ConditionalCheckFailedException e) {
            throw new IllegalArgumentException("username already exists");
        }

        return record;
    }
}
